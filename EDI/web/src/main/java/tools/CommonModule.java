package tools;

import abstract_entity.AbstractDocumentEdi;
import app_info.Constant;
import categories.UploadedFile;
import hibernate.impl.documents.AbstractDocumentEdiImpl;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * Created by kostya on 1/26/2017.
 */
public enum CommonModule {

    INSTANCE;

    public static String getCorrectStringForWeb(String base) {
       if (Objects.isNull(base)) return "";
       else return base.replace("\r\n", "</br>").replace("'","").replace("\\","").replace("\"","");
    }

    public static String getCorrectStringJspPage(String base) {

        // Should be replace("\r\n", "") ( should not be replace("\r\n", "</br>")) cause a lot of text can't change its position (at the head of *_view.jsp)
        // Should be replace("\"","\'") cause in html we have: document.getElementById("review_document").innerHTML = "<html><div></div> ... </html>";
        // and in this string can't be symbol " in string before main ending with "
        return base.replace("\r\n", "").replace("\"","\'").replace("\r","");
    }

    public static String getReplyString(String base){
        base = " \r\n>>> ".concat(base);
        return base.replace("</br>","\r\n>>> ");
    }

    public static <T extends Number> Number getNumberFromRequest(HttpServletRequest req, String parameterName, Class<T> type) {

        String parameterString = req.getParameter(parameterName);

        if (Objects.nonNull(parameterString)) {
            if (parameterString.isEmpty()) return null;
            else if (type.equals(Integer.class)) return Integer.valueOf(parameterString);
            else if (type.equals(Long.class)) return Long.valueOf(parameterString);
            else if (type.equals(Double.class)) return Double.valueOf(parameterString);
            else if (type.equals(Float.class)) return Float.valueOf(parameterString);
        }

        return null;
    }

    // the same as jsp - common - common.jsp
    private static int getRandomInt() {
        int min = 0;
        int max = (int) Math.pow(2, 30);
        return ThreadLocalRandom.current().nextInt(min, max + 1);   // http://stackoverflow.com/questions/363681/how-to-generate-random-integers-within-a-specific-range-in-java
    }

    // Will be replaced by UserSettings.setMapSort(Map<String, String> mapSort, String mapName, StringBuilder sortColumnNumber){
    public void replaceSortingParameter(Map<String, String> mapSort, String bookmarkInMap, StringBuilder sortColumnNumber) {

        if (Objects.nonNull(sortColumnNumber) && (sortColumnNumber.length() == 3) && Objects.nonNull(mapSort.get(bookmarkInMap))) {
            if (sortColumnNumber.charAt(0) == mapSort.get(bookmarkInMap).charAt(0)) {
                switch ( mapSort.get(bookmarkInMap).charAt(2)) {
                    case 'n':
                        sortColumnNumber.replace(2, 3, "-");
                        break;
                    case '+':
                        sortColumnNumber.replace(2, 3, "-");
                        break;
                    case '-':
                        sortColumnNumber.replace(2, 3, "+");
                        break;
                }
            }
            mapSort.put(bookmarkInMap, sortColumnNumber.toString());
        }

    }

    public static String getDocumentLinkView(String str){

//      Example:
//      String str =
//        "Посмотри служебку http://localhost:8080/executor_task?documentId=1078&executorTaskId=1094&tempId=754240378там все написано\n" +
//        " или эту http://localhost:8080/executor_task?documentId=1078&executorTaskId=1094&tempId=634340378"
//      Output:
//        "Посмотри служебку <a href=http://localhost:8080/executor_task?documentId=1078&tempId=2392323230</a>там все написано\n"
//       " или эту <a href=/executor_task?documentId=1078&tempId=243432356</a>"
//      P.s.
//      Great site for regex: https://regexone.com/references/java

        Pattern patternHref = Pattern.compile("(http://)[^а-яА-Я: ]+:[0-9]+(/executor_task/?).[^ а-яА-Я]+");
        Pattern patternDocumentNumber = Pattern.compile("(documentId=).[0-9]+");

        Matcher matcherHref = patternHref.matcher(str);

        while (matcherHref.find()) {
            AbstractDocumentEdi documentEdi = null;
            Matcher matcherDocumentNumber = patternDocumentNumber.matcher(matcherHref.group());
            if (matcherDocumentNumber.find()) {
                Long documentId = Long.valueOf(matcherDocumentNumber.group().replace("documentId=",""));
                if (Objects.nonNull(documentId)) documentEdi = AbstractDocumentEdiImpl.INSTANCE.getById(documentId);
            }

            // It might be something like that "pageContext.request.contextPath" in server name (probably for Linux system)
            str = str.replaceFirst(patternHref.toString(),
                    "<a href= /executor_task?" +matcherDocumentNumber.group()+"&tempId="+String.valueOf(getRandomInt()) + " target='_blank'><img src='/resources/images/ref_document.png'>"+(Objects.nonNull(documentEdi)? documentEdi.getDocumentView("dd.MM.yyyy HH:mm:ss"):"Документ")+"</a> ");

        }

        str = getCorrectStringForWeb(str);

        return str;

    }

    public static List<UploadedFile> getFileListFromRequest(HttpServletRequest req, String parameterName) throws IOException, ServletException {

        final String pathString = Constant.BASIC_FILE_PATH + Constant.PATH_YEAR_MONTH;

        List<UploadedFile> result = new ArrayList<>();
        List<Part> fileParts = req.getParts().stream().filter(part -> parameterName.equals(part.getName())).collect(Collectors.toList());
        if (!fileParts.isEmpty()) {

            for (Part filePart : fileParts) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                try {
                    Path path = Paths.get(pathString + fileName);
                    String md5Hex;
                    try (InputStream reader = filePart.getInputStream())
                    {
                        Files.copy(reader, path);
                        FileInputStream fis = new FileInputStream(path.toFile());
                        md5Hex = DigestUtils.md5Hex(fis);
                        fis.close();
                    }

                    Path pathNew = Paths.get(pathString + md5Hex);
                    Files.move(path, pathNew, StandardCopyOption.REPLACE_EXISTING);

                    result.add(new UploadedFile(fileName, false, 0L, false, md5Hex, Constant.PATH_YEAR_MONTH+md5Hex, null, null));  //  new 5/23/2017

                } catch (FileNotFoundException e) {
                    System.out.println("Error in uploading a file");
                }

            }
        }

        return result;
    }


    // ********* Lambda begin ***********************
    // Today it's here but someday it'll probably be in the service

    //http://stackoverflow.com/questions/23057549/lambda-expression-to-convert-array-list-of-string-to-array-list-of-integers
    //for arrays
    public static <T, U> U[] convertArray(T[] from, Function<T, U> func, IntFunction<U[]> generator) {
        return Arrays.stream(from).filter(t -> (t != null)&&!("".equals(t))).map(func).toArray(generator);
    }

    // ********* Lambda end ***********************
}
