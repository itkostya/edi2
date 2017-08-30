package service_tools;

import business_processes.BusinessProcess;
import business_processes.BusinessProcessSequence;
import business_processes.ExecutorTask;
import business_processes.ExecutorTaskFolderStructure;
import categories.Department;
import categories.Position;
import categories.User;
import documents.Memorandum;
import documents.Message;
import enumerations.FolderStructure;
import enumerations.ProcessOrderType;
import enumerations.ProcessResult;
import enumerations.ProcessType;
import hibernate.impl.business_processes.BusinessProcessImpl;
import hibernate.impl.business_processes.BusinessProcessSequenceImpl;
import hibernate.impl.business_processes.ExecutorTaskFolderStructureImpl;
import hibernate.impl.business_processes.ExecutorTaskImpl;
import hibernate.impl.categories.DepartmentImpl;
import hibernate.impl.categories.PositionImpl;
import hibernate.impl.categories.UserImpl;
import hibernate.impl.documents.MemorandumImpl;
import hibernate.impl.documents.MessageImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Created by kostya on 9/2/2016.
 */
public enum CreateData {

    INSTANCE;

    public static void createDepartments() {

        DepartmentImpl.INSTANCE.save(new Department("It"));
        DepartmentImpl.INSTANCE.save(new Department("HR"));

    }

    public static void createPositions() {

        PositionImpl.INSTANCE.save(new Position("Programmer"));
        PositionImpl.INSTANCE.save(new Position("HR manager"));
    }

    public static void createUsers() {

//        UserImpl.INSTANCE.save(new User("kostya", false, null, false, "Zhurov", "Kostyantin", "Oleksandrovich", "kostya", "DC", null, null, null));
//        UserImpl.INSTANCE.save(new User("itkostya", false, null, false, "Zhurov", "Kostyantin", "Oleksandrovich", "itkostya", "DC", "123", null, null));
//        UserImpl.INSTANCE.save(new User("hr_manager", false, null, false, "Manager", "Managerovich", "Managov", "hr_manager", "DC", "", null, null));


        UserImpl.INSTANCE.save(new User("stat1", false, null, false, "Stat1", "Statistic1", "Statistikovich1", "stat1", null, "123", null, null));
        UserImpl.INSTANCE.save(new User("stat2", false, null, false, "Stat2", "Statistic2", "Statistikovich2", "stat2", null, "123", null, null));
        UserImpl.INSTANCE.save(new User("stat3", false, null, false, "Stat3", "Statistic3", "Statistikovich3", "stat3", null, "123", null, null));
        UserImpl.INSTANCE.save(new User("stat4", false, null, false, "Stat4", "Statistic4", "Statistikovich4", "stat4", null, "123", null, null));
        UserImpl.INSTANCE.save(new User("stat5", false, null, false, "Stat5", "Statistic5", "Statistikovich5", "stat5", null, "123", null, null));
        UserImpl.INSTANCE.save(new User("stat6", false, null, false, "Stat6", "Statistic6", "Statistikovich6", "stat6", null, "123", null, null));

    }

    public static void createCategories() {

        // Departments
        Department departmentIt = new Department("It");
        DepartmentImpl.INSTANCE.save(departmentIt);
        Department departmentHr = new Department("HR");
        DepartmentImpl.INSTANCE.save(departmentHr);

        // Positions
        Position positionItDirector = new Position("It Director");
        PositionImpl.INSTANCE.save(positionItDirector);
        Position positionProgrammer = new Position("Programmer");
        PositionImpl.INSTANCE.save(positionProgrammer);
        Position positionAdmin = new Position("System Engineer");
        PositionImpl.INSTANCE.save(positionAdmin);

        Position positionHrDirector = new Position("HR Director");
        PositionImpl.INSTANCE.save(positionHrDirector);
        Position positionHrManager = new Position("HR Manager");
        PositionImpl.INSTANCE.save(positionHrManager);

        // Users
        UserImpl.INSTANCE.save(new User("kostya", false, null, false, "Zhurov", "Kostyantin", "Oleksandrovich", "kostya", "DC", null, null, null));

        UserImpl.INSTANCE.save(new User("itkostya", false, null, false, "Журов", "Константин", "Александрович", "itkostya", null, "123", positionProgrammer, departmentIt));
        UserImpl.INSTANCE.save(new User("it_director", false, null, false, "Чегалкин", "Сергей", "Викторович", "it_director", null, "123", positionItDirector, departmentIt));
        UserImpl.INSTANCE.save(new User("it_admin1", false, null, false, "Капитошка", "Виталий", "Владимирович", "it_admin1", null, "123", positionAdmin, departmentIt));
        UserImpl.INSTANCE.save(new User("it_admin2", false, null, false, "Рябчиков", "Сергей", "Владимирович", "it_admin2", null, "123", positionAdmin, departmentIt));

        UserImpl.INSTANCE.save(new User("hr_director", false, null, false, "Drocenko", "Ekaterina", "Valerievna", "hr_director", null, "234", positionHrDirector, departmentHr));
        UserImpl.INSTANCE.save(new User("hr_manager1", false, null, false, "Kruglenko", "Olga", "Aleksandrovna", "hr_manager1", null, "234", positionHrManager, departmentHr));
        UserImpl.INSTANCE.save(new User("hr_manager2", false, null, false, "Karpova", "Julia", "Aleksandrovna", "hr_manager2", null, "234", positionHrManager, departmentHr));

    }

    public static void createMemorandums() {

        // Memorandums

        User user_itkostya = UserImpl.INSTANCE.getUserByLogin("itkostya");
        User it_admin1 = UserImpl.INSTANCE.getUserByLogin("it_admin1");
        User it_admin2 = UserImpl.INSTANCE.getUserByLogin("it_admin2");
        User it_director = UserImpl.INSTANCE.getUserByLogin("it_director");
        User hr_director = UserImpl.INSTANCE.getUserByLogin("hr_director");
        User hr_manager1 = UserImpl.INSTANCE.getUserByLogin("hr_manager1");
        User hr_manager2 = UserImpl.INSTANCE.getUserByLogin("hr_manager2");

        java.sql.Timestamp d1 = java.sql.Timestamp.valueOf("2015-01-21 01:00:00");
        java.sql.Timestamp d2 = java.sql.Timestamp.valueOf("2015-01-23 01:00:00");
        java.sql.Timestamp d3 = java.sql.Timestamp.valueOf("2015-02-25 01:00:00");
        java.sql.Timestamp d4 = java.sql.Timestamp.valueOf("2015-02-26 01:00:00");
        java.sql.Timestamp d5 = java.sql.Timestamp.valueOf("2015-02-27 01:00:00");
        java.sql.Timestamp d6 = java.sql.Timestamp.valueOf("2015-03-07 01:00:00");
        java.sql.Timestamp d7 = java.sql.Timestamp.valueOf("2015-03-11 01:00:00");
        java.sql.Timestamp d8 = java.sql.Timestamp.valueOf("2015-03-12 01:00:00");
        java.sql.Timestamp d9 = java.sql.Timestamp.valueOf("2015-03-15 01:00:00");
        java.sql.Timestamp d10 = java.sql.Timestamp.valueOf("2015-03-25 01:00:00");

        MemorandumImpl.INSTANCE.save(new Memorandum(d1, false, null, false, user_itkostya, it_director,"Анкета_СЭД", "Анкета Документооборот"));
        MemorandumImpl.INSTANCE.save(new Memorandum(d2, false, null, false, user_itkostya, it_director, "Перепопенко", "Подключение сотрудника к Документообороту"));
        MemorandumImpl.INSTANCE.save(new Memorandum(d3, false, null, false, user_itkostya, it_director, "Хачатурян", "Подключение пользователя к домену"));
        MemorandumImpl.INSTANCE.save(new Memorandum(d4, false, null, false, user_itkostya, it_director, "Овчаренко", "Подключение к домену"));

        MemorandumImpl.INSTANCE.save(new Memorandum(d2, false, null, false, it_admin1, it_director, "Логины сотрудников", "Список сотрудников: Василий Сысоев, Ахтандег Гамандрилов"));
        MemorandumImpl.INSTANCE.save(new Memorandum(d3, false, null, false, it_admin1, it_director, "Настройки пользователя", "Укажите отдел сотрудника Сысоев"));
        MemorandumImpl.INSTANCE.save(new Memorandum(d4, false, null, false, it_admin1, it_director, "Настройки Лига", "Зачем бухгалтеру Лига? Пусть спрашивает у юриста"));
        MemorandumImpl.INSTANCE.save(new Memorandum(d5, false, null, false, it_admin1, it_director, "Настройки Medoc", "Зачем Вам Медок? Носите дискеты в налоговую!"));

        MemorandumImpl.INSTANCE.save(new Memorandum(d3, false, null, false, it_admin2, it_director, "Тест", "Тест 123"));
        MemorandumImpl.INSTANCE.save(new Memorandum(d4, false, null, false, it_admin2, it_director, "Молоток", "Где мой молоток, товарисчи?"));
        MemorandumImpl.INSTANCE.save(new Memorandum(d5, false, null, false, it_admin2, it_director, "Приколы свежак", "Зацените https://www.youtube.com/watch?v=P1qRmJkHFmQ"));
        MemorandumImpl.INSTANCE.save(new Memorandum(d6, false, null, false, it_admin2, it_director, "Женщины", "Дорогие женщины, чисто сердечно поздравляем Вас с праздником весны - 8 Марта!"));

        MemorandumImpl.INSTANCE.save(new Memorandum(d4, false, null, false, hr_director, hr_director, "Анкета_СЭД", "Анкета Документооборот"));
        MemorandumImpl.INSTANCE.save(new Memorandum(d5, false, null, false, hr_director,  hr_director, "Отчеты за месяц", "Любезные мои подчиненные, а не соблаговолите ли Вы предоставить мне свои отчеты за месяц в течении 5 минут"));
        MemorandumImpl.INSTANCE.save(new Memorandum(d6, false, null, false, hr_director,  hr_director,"Коллеги с 8 Марта", "Чтоб Вас мужики любили и на руках носили!"));

        MemorandumImpl.INSTANCE.save(new Memorandum(d7, false, null, false, hr_manager1,  hr_director,"Анкета_СЭД", "Анкета Документооборот"));
        MemorandumImpl.INSTANCE.save(new Memorandum(d8, false, null, false, hr_manager1,  hr_director,"Отчет о работе за месяц", "Процент выполнения своих задач: 110%"));
        MemorandumImpl.INSTANCE.save(new Memorandum(d9, false, null, false, hr_manager1,  hr_director,"Срез данных по з/п", "В соответстии с последним срезом данных ИТ отделу необходимо поднять з/п на 50% с оплатой в долларах"));

        MemorandumImpl.INSTANCE.save(new Memorandum(d6, false, null, false, hr_manager2, hr_manager2, "С праздником!", "Девченки, с праздником! P.s. Все мужики - козлы ;-)"));
        MemorandumImpl.INSTANCE.save(new Memorandum(d10, false, null, false, hr_manager2, hr_manager1,"Декрет", "Я ухожу в декрет! Мне было приятно с Вами всеми работать! Вы такие классные! Ня... )"));
    }

    public static void createBusinessProcess() {

        java.sql.Date d1 = java.sql.Date.valueOf("2015-01-21");
        java.sql.Date d2 = java.sql.Date.valueOf("2015-01-23");
        java.sql.Date d3 = java.sql.Date.valueOf("2015-02-25");

        java.sql.Timestamp t1 = java.sql.Timestamp.valueOf("2015-01-21 09:05:00");
        java.sql.Timestamp t2 = java.sql.Timestamp.valueOf("2015-02-21 19:05:00");
        java.sql.Timestamp t3 = java.sql.Timestamp.valueOf("2015-03-19 15:08:07");

        User user_itkostya = UserImpl.INSTANCE.getUserByLogin("itkostya");
        Memorandum m1 = new Memorandum(t1, false, null, false, user_itkostya, user_itkostya,"Служебка тест БП", "Служебка тест БП");
        MemorandumImpl.INSTANCE.save(m1);

        User it_admin1 = UserImpl.INSTANCE.getUserByLogin("it_admin1");
        User it_admin2 = UserImpl.INSTANCE.getUserByLogin("it_admin2");

        BusinessProcessImpl.INSTANCE.save(new BusinessProcess(t1, false, user_itkostya, t1, m1, "My comment", ProcessResult.ACCOMMODATED));
        BusinessProcessImpl.INSTANCE.save(new BusinessProcess(t2, false, user_itkostya, t2, m1, "Привет привет", ProcessResult.AFFIRMED));
        BusinessProcessImpl.INSTANCE.save(new BusinessProcess(t3, false, it_admin1, t3, m1, "Коммент", ProcessResult.AFFIRMED));
        BusinessProcessImpl.INSTANCE.save(new BusinessProcess(t1, false, it_admin1, t1, m1, "Вот так", ProcessResult.POSITIVE));
        BusinessProcessImpl.INSTANCE.save(new BusinessProcess(t2, false, it_admin2, t2, m1, "Ну и так", ProcessResult.ACCOMMODATED));
        BusinessProcessImpl.INSTANCE.save(new BusinessProcess(t3, false, it_admin2, t3, m1, "И еще", ProcessResult.NEGATIVE));

    }

    public static void createBusinessProcessSequence() {

        java.sql.Date d1 = java.sql.Date.valueOf("2015-01-21");
        java.sql.Date d2 = java.sql.Date.valueOf("2015-01-23");
        java.sql.Date d3 = java.sql.Date.valueOf("2015-02-25");

        java.sql.Timestamp t1 = java.sql.Timestamp.valueOf("2015-01-21 09:05:00");
        java.sql.Timestamp t2 = java.sql.Timestamp.valueOf("2015-02-21 19:05:00");
        java.sql.Timestamp t3 = java.sql.Timestamp.valueOf("2015-03-19 15:08:07");

        User user_itkostya = UserImpl.INSTANCE.getUserByLogin("itkostya");
        Memorandum m1 = new Memorandum(t1, false, null, false, user_itkostya, user_itkostya,"Служебка тест БП", "Служебка тест БП");
        MemorandumImpl.INSTANCE.save(m1);

        User it_admin1 = UserImpl.INSTANCE.getUserByLogin("it_admin1");
        User it_admin2 = UserImpl.INSTANCE.getUserByLogin("it_admin2");

        BusinessProcess bp1 = new BusinessProcess(t1, false, user_itkostya, t1, m1, "БП 1", ProcessResult.ACCOMMODATED);
        BusinessProcessImpl.INSTANCE.save(bp1);

        BusinessProcess bp2 = new BusinessProcess(t2, false, it_admin1, t2, m1, "БП 2", ProcessResult.CANCELED);
        BusinessProcessImpl.INSTANCE.save(bp2);

        BusinessProcess bp3 = new BusinessProcess(t3, false, it_admin2, t3, m1, "БП 3", ProcessResult.DECLINED);
        BusinessProcessImpl.INSTANCE.save(bp3);

        BusinessProcessSequenceImpl.INSTANCE.save(new BusinessProcessSequence(t1, bp1, user_itkostya, false, ProcessOrderType.AFTER, ProcessResult.ACCOMMODATED, ProcessType.EXECUTION, false,null));
        BusinessProcessSequenceImpl.INSTANCE.save(new BusinessProcessSequence(t1, bp1, it_admin1, false, ProcessOrderType.TOGETHER, ProcessResult.CANCELED, ProcessType.INFORMATION, false, null));
        BusinessProcessSequenceImpl.INSTANCE.save(new BusinessProcessSequence(t1, bp1, it_admin2, false, ProcessOrderType.TOGETHER, ProcessResult.POSITIVE, ProcessType.VISA, false, null));

        BusinessProcessSequenceImpl.INSTANCE.save(new BusinessProcessSequence(t2, bp2, user_itkostya, false, ProcessOrderType.AFTER, ProcessResult.ACCOMMODATED, ProcessType.EXECUTION, false, null));
        BusinessProcessSequenceImpl.INSTANCE.save(new BusinessProcessSequence(t2, bp2, it_admin1, false, ProcessOrderType.TOGETHER, ProcessResult.CANCELED, ProcessType.INFORMATION, false, null));
        BusinessProcessSequenceImpl.INSTANCE.save(new BusinessProcessSequence(t2, bp2, it_admin2, false, ProcessOrderType.TOGETHER, ProcessResult.POSITIVE, ProcessType.VISA, false, null));

        BusinessProcessSequenceImpl.INSTANCE.save(new BusinessProcessSequence(t3, bp3, user_itkostya, false, ProcessOrderType.AFTER, ProcessResult.ACCOMMODATED, ProcessType.EXECUTION, false, null));
        BusinessProcessSequenceImpl.INSTANCE.save(new BusinessProcessSequence(t3, bp3, it_admin1, false, ProcessOrderType.TOGETHER, ProcessResult.CANCELED, ProcessType.INFORMATION, false, null));
        BusinessProcessSequenceImpl.INSTANCE.save(new BusinessProcessSequence(t3, bp3, it_admin2, false, ProcessOrderType.TOGETHER, ProcessResult.POSITIVE, ProcessType.VISA, false, null));

    }

    public static void createExecutorTasks() {

        java.sql.Date d1 = java.sql.Date.valueOf("2015-01-21");
        java.sql.Date d2 = java.sql.Date.valueOf("2015-01-23");
        java.sql.Date d3 = java.sql.Date.valueOf("2015-02-25");

        java.sql.Timestamp t1 = java.sql.Timestamp.valueOf("2015-01-21 09:05:00");
        java.sql.Timestamp t2 = java.sql.Timestamp.valueOf("2015-02-21 19:05:00");
        java.sql.Timestamp t3 = java.sql.Timestamp.valueOf("2015-03-19 15:08:07");
        java.sql.Timestamp t4 = java.sql.Timestamp.valueOf("2015-04-01 12:02:47");

        User user_itkostya = UserImpl.INSTANCE.getUserByLogin("itkostya");
        Memorandum m1 = new Memorandum(t1, false, null, false, user_itkostya, user_itkostya,"Служебка тест БП", "Служебка тест БП");
        MemorandumImpl.INSTANCE.save(m1);

        Message message1 = new Message(t1, false, null, false, user_itkostya, user_itkostya,"Сообщение тест", "Сообщение тест");
        MessageImpl.INSTANCE.save(message1);

        User it_admin1 = UserImpl.INSTANCE.getUserByLogin("it_admin1");
        User it_admin2 = UserImpl.INSTANCE.getUserByLogin("it_admin2");

        BusinessProcess bp1 = new BusinessProcess(t1, false, user_itkostya, t1, m1, "БП 1", ProcessResult.ACCOMMODATED);
        BusinessProcessImpl.INSTANCE.save(bp1);

        BusinessProcess bp2 = new BusinessProcess(t2, false, it_admin1, t2, m1, "БП 2", ProcessResult.CANCELED);
        BusinessProcessImpl.INSTANCE.save(bp2);

        BusinessProcess bp3 = new BusinessProcess(t3, false, it_admin2, t3, m1, "БП 3", ProcessResult.DECLINED);
        BusinessProcessImpl.INSTANCE.save(bp3);

        BusinessProcess bp4 = new BusinessProcess(t1, false, user_itkostya, t1, message1, "БП 4", ProcessResult.ACCOMMODATED);
        BusinessProcessImpl.INSTANCE.save(bp4);

        BusinessProcess bp5 = new BusinessProcess(t2, false, it_admin1, t2, message1, "БП 5", ProcessResult.CANCELED);
        BusinessProcessImpl.INSTANCE.save(bp5);

        BusinessProcess bp6 = new BusinessProcess(t3, false, it_admin2, t3, message1, "БП 6", ProcessResult.DECLINED);
        BusinessProcessImpl.INSTANCE.save(bp6);

        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t1, bp1, false, user_itkostya, m1, t2, "Коммент 1", ProcessResult.POSITIVE, ProcessType.INFORMATION, null, user_itkostya, false, false,false));
        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t1, bp1, false, user_itkostya, m1, t2, "Коммент 2", ProcessResult.POSITIVE, ProcessType.INFORMATION, null,it_admin1, false, false,false));
        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t1, bp1, false, user_itkostya, m1, t2, "Коммент 3", ProcessResult.POSITIVE, ProcessType.INFORMATION, null,it_admin2, false, false,false));

        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t2, bp2, false, it_admin1, m1, t3, "Коммент 4", ProcessResult.NEGATIVE, ProcessType.EXECUTION, null,user_itkostya, false, false,false));
        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t2, bp2, false, it_admin1, m1, t3, "Коммент 5", ProcessResult.COMPLETED, ProcessType.EXECUTION, null,it_admin1, false, false,false));
        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t2, bp2, false, it_admin1, m1, t3, "Коммент 6", ProcessResult.INFORMED, ProcessType.EXECUTION, null,it_admin2, false, false,false));

        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t3, bp3, false, it_admin2, m1, t4, "Коммент 7", ProcessResult.POSITIVE, ProcessType.PAYMENT, null,user_itkostya, false, false,false));
        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t3, bp3, false, it_admin2, m1, t4, "Коммент 8", ProcessResult.POSITIVE, ProcessType.PAYMENT, null,it_admin1, false, false,false));
        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t3, bp3, false, it_admin2, m1, t4, "Коммент 9", ProcessResult.POSITIVE, ProcessType.PAYMENT, null,it_admin2, false, false,false));

        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t1, bp4, false, user_itkostya, message1, t2, "Коммент 11", ProcessResult.POSITIVE, ProcessType.INFORMATION, null,user_itkostya, false, false,false));
        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t1, bp4, false, user_itkostya, message1, t2, "Коммент 12", ProcessResult.POSITIVE, ProcessType.INFORMATION, null,it_admin1, false, false,false));
        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t1, bp4, false, user_itkostya, message1, t2, "Коммент 13", ProcessResult.POSITIVE, ProcessType.INFORMATION, null,it_admin2, false, false,false));

        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t2, bp5, false, it_admin1, message1, t3, "Коммент 14", ProcessResult.NEGATIVE, ProcessType.EXECUTION, null, user_itkostya, false, false,false));
        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t2, bp5, false, it_admin1, message1, t3, "Коммент 15", ProcessResult.COMPLETED, ProcessType.EXECUTION, null, it_admin1, false, false,false));
        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t2, bp5, false, it_admin1, message1, t3, "Коммент 16", ProcessResult.INFORMED, ProcessType.EXECUTION, null, it_admin2, false, false,false));

        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t3, bp6, false, it_admin2, message1, t4, "Коммент 17", ProcessResult.POSITIVE, ProcessType.PAYMENT, null, user_itkostya, false, false,false));
        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t3, bp6, false, it_admin2, message1, t4, "Коммент 18", ProcessResult.POSITIVE, ProcessType.PAYMENT, null, it_admin1, false, false,false));
        ExecutorTaskImpl.INSTANCE.save(new ExecutorTask(t3, bp6, false, it_admin2, message1, t4, "Коммент 19", ProcessResult.POSITIVE, ProcessType.PAYMENT, null, it_admin2, false, false,false));

    }

    public static void createAll() {

        // Departments
        Department departmentIt = new Department("It");
        DepartmentImpl.INSTANCE.save(departmentIt);
        Department departmentHr = new Department("HR");
        DepartmentImpl.INSTANCE.save(departmentHr);

        // Positions
        Position positionItDirector = new Position("It Director");
        PositionImpl.INSTANCE.save(positionItDirector);
        Position positionProgrammer = new Position("Programmer");
        PositionImpl.INSTANCE.save(positionProgrammer);
        Position positionAdmin = new Position("System Engineer");
        PositionImpl.INSTANCE.save(positionAdmin);

        Position positionHrDirector = new Position("HR Director");
        PositionImpl.INSTANCE.save(positionHrDirector);
        Position positionHrManager = new Position("HR Manager");
        PositionImpl.INSTANCE.save(positionHrManager);

        // Users
        UserImpl.INSTANCE.save(new User("kostya", false, null, false, "Zhurov", "Kostyantin", "Oleksandrovich", "kostya", "DC", null, null, null));

        User user_itkostya = new User("itkostya", false, null, false, "Журов", "Константин", "Александрович", "itkostya", null, "123", positionProgrammer, departmentIt);
        User it_admin1 = new User("it_admin1", false, null, false, "Капитошка", "Виталий", "Владимирович", "it_admin1", null, "123", positionAdmin, departmentIt);
        User it_admin2 = new User("it_admin2", false, null, false, "Рябчиков", "Сергей", "Владимирович", "it_admin2", null, "123", positionAdmin, departmentIt);
        User it_director = new User("it_director", false, null, false, "Чегалкин", "Сергей", "Викторович", "it_director", null, "123", positionItDirector, departmentIt);

        User hr_director = new User("hr_director", false, null, false, "Drocenko", "Ekaterina", "Valerievna", "hr_director", null, "234", positionHrDirector, departmentHr);
        User hr_manager1 = new User("hr_manager1", false, null, false, "Kruglenko", "Olga", "Aleksandrovna", "hr_manager1", null, "234", positionHrManager, departmentHr);
        User hr_manager2 = new User("hr_manager2", false, null, false, "Karpova", "Julia", "Aleksandrovna", "hr_manager2", null, "234", positionHrManager, departmentHr);

        UserImpl.INSTANCE.save(user_itkostya);
        UserImpl.INSTANCE.save(it_admin1);
        UserImpl.INSTANCE.save(it_admin2);
        UserImpl.INSTANCE.save(it_director);

        UserImpl.INSTANCE.save(hr_director);
        UserImpl.INSTANCE.save(hr_manager1);
        UserImpl.INSTANCE.save(hr_manager2);

        java.sql.Date d1 = java.sql.Date.valueOf("2015-01-21");
        java.sql.Date d2 = java.sql.Date.valueOf("2015-01-23");
        java.sql.Date d3 = java.sql.Date.valueOf("2015-02-25");

        java.sql.Timestamp t1 = java.sql.Timestamp.valueOf("2015-01-21 09:05:00");
        java.sql.Timestamp t2 = java.sql.Timestamp.valueOf("2015-02-21 19:05:00");
        java.sql.Timestamp t3 = java.sql.Timestamp.valueOf("2015-03-19 15:08:07");
        java.sql.Timestamp t4 = java.sql.Timestamp.valueOf("2015-04-01 12:02:47");
        java.sql.Timestamp t5 = java.sql.Timestamp.valueOf("2015-02-27 01:00:00");
        java.sql.Timestamp t6 = java.sql.Timestamp.valueOf("2015-03-07 01:00:00");
        java.sql.Timestamp t7 = java.sql.Timestamp.valueOf("2015-03-11 01:00:00");
        java.sql.Timestamp t8 = java.sql.Timestamp.valueOf("2015-03-12 01:00:00");
        java.sql.Timestamp t9 = java.sql.Timestamp.valueOf("2015-03-15 01:00:00");
        java.sql.Timestamp t10 = java.sql.Timestamp.valueOf("2015-03-25 01:00:00");

        List<Memorandum> memorandumList = new ArrayList<>(
                Arrays.asList(
                        new Memorandum(t1, false, null, false, user_itkostya, it_director, "Анкета_СЭД", "Анкета Документооборот"),
                        new Memorandum(t2, false, null, false, user_itkostya, it_director, "Перепопенко", "Подключение сотрудника к Документообороту"),
                        new Memorandum(t3, false, null, false, user_itkostya, it_director, "Хачатурян", "Подключение пользователя к домену"),
                        new Memorandum(t4, false, null, false, user_itkostya, it_director, "Овчаренко", "Подключение к домену"),
                        new Memorandum(t2, false, null, false, it_admin1, it_director, "Логины сотрудников", "Список сотрудников: Василий Сысоев, Ахтандег Гамандрилов"),
                        new Memorandum(t3, false, null, false, it_admin1, it_director, "Настройки пользователя", "Укажите отдел сотрудника Сысоев"),
                        new Memorandum(t4, false, null, false, it_admin1, it_director, "Настройки Лига", "Зачем бухгалтеру Лига? Пусть спрашивает у юриста"),
                        new Memorandum(t5, false, null, false, it_admin1, it_director, "Настройки Medoc", "Зачем Вам Медок? Носите дискеты в налоговую!"),
                        new Memorandum(t3, false, null, false, it_admin2, it_director, "Тест", "Тест 123"),
                        new Memorandum(t4, false, null, false, it_admin2, it_director, "Молоток", "Где мой молоток, товарисчи?"),
                        new Memorandum(t5, false, null, false, it_admin2, it_director, "Приколы свежак", "Зацените https://www.youtube.com/watch?v=P1qRmJkHFmQ"),
                        new Memorandum(t6, false, null, false, it_admin2, hr_director, "Женщины", "Дорогие женщины, чисто сердечно поздравляем Вас с праздником весны - 8 Марта!"),
                        new Memorandum(t4, false, null, false, hr_director, it_director, "Анкета_СЭД", "Анкета Документооборот"),
                        new Memorandum(t5, false, null, false, hr_director, it_director, "Отчеты за месяц", "Любезные мои подчиненные, а не соблаговолите ли Вы предоставить мне свои отчеты за месяц в течении 5 минут"),
                        new Memorandum(t6, false, null, false, hr_director, it_director, "Коллеги с 8 Марта", "Чтоб Вас мужики любили и на руках носили!"),
                        new Memorandum(t7, false, null, false, hr_manager1, it_director, "Анкета_СЭД", "Анкета Документооборот"),
                        new Memorandum(t8, false, null, false, hr_manager1, it_director, "Отчет о работе за месяц", "Процент выполнения своих задач: 110%"),
                        new Memorandum(t9, false, null, false, hr_manager1, it_director, "Срез данных по з/п", "В соответстии с последним срезом данных ИТ отделу необходимо поднять з/п на 50% с оплатой в долларах"),
                        new Memorandum(t6, false, null, false, hr_manager2, hr_manager1, "С праздником!", "Девченки, с праздником! P.s. Все мужики - козлы ;-)"),
                        new Memorandum(t10, false, null, false, hr_manager2, hr_manager1, "Декрет", "Я ухожу в декрет! Мне было приятно с Вами всеми работать! Вы такие классные! Ня... )")));
        for (Memorandum memorandum : memorandumList) MemorandumImpl.INSTANCE.save(memorandum);

        Message message1 = new Message(t1, false, null, false, user_itkostya,  user_itkostya,"Сообщение тест", "Сообщение тест");
        MessageImpl.INSTANCE.save(message1);

        List<BusinessProcess> businessProcessList = new ArrayList<>(
                Arrays.asList(
                        new BusinessProcess(t1, false, user_itkostya, t1, memorandumList.get(0), "БП 1", ProcessResult.ACCOMMODATED),
                        new BusinessProcess(t1, false, user_itkostya, t1, memorandumList.get(0), "БП 1", ProcessResult.ACCOMMODATED),
                        new BusinessProcess(t2, false, it_admin1, t2, memorandumList.get(1), "БП 2", ProcessResult.CANCELED),
                        new BusinessProcess(t3, false, it_admin2, t3, memorandumList.get(0), "БП 3", ProcessResult.DECLINED),
                        new BusinessProcess(t1, false, user_itkostya, t1, message1, "БП 4", ProcessResult.ACCOMMODATED),
                        new BusinessProcess(t2, false, it_admin1, t2, message1, "БП 5", ProcessResult.CANCELED),
                        new BusinessProcess(t3, false, it_admin2, t3, message1, "БП 6", ProcessResult.DECLINED)));
        for (BusinessProcess businessProcess : businessProcessList) BusinessProcessImpl.INSTANCE.save(businessProcess);

        List<ExecutorTask> executorTaskList = new ArrayList<>(
                Arrays.asList(
                        new ExecutorTask(t1, businessProcessList.get(0), false, user_itkostya, memorandumList.get(0), t2, "Коммент 1", ProcessResult.POSITIVE, ProcessType.INFORMATION, null, user_itkostya, false, false, false),
                        new ExecutorTask(t1, businessProcessList.get(0), false, user_itkostya, memorandumList.get(0), t2, "Коммент 2", ProcessResult.POSITIVE, ProcessType.INFORMATION, null, it_admin1, false, false, false),
                        new ExecutorTask(t1, businessProcessList.get(0), false, user_itkostya, memorandumList.get(0), t2, "Коммент 3", ProcessResult.POSITIVE, ProcessType.INFORMATION, null, it_admin2, false, false, false),
                        new ExecutorTask(t2, businessProcessList.get(1), false, it_admin1, memorandumList.get(0), t3, "Коммент 4", ProcessResult.NEGATIVE, ProcessType.EXECUTION, null, user_itkostya, false, false, false),
                        new ExecutorTask(t2, businessProcessList.get(1), false, it_admin1, memorandumList.get(0), t3, "Коммент 5", ProcessResult.COMPLETED, ProcessType.EXECUTION, null, it_admin1, false, false, false),
                        new ExecutorTask(t2, businessProcessList.get(1), false, it_admin1, memorandumList.get(0), t3, "Коммент 6", ProcessResult.INFORMED, ProcessType.EXECUTION, null, it_admin2, false, false, false),
                        new ExecutorTask(t3, businessProcessList.get(2), false, it_admin2, memorandumList.get(0), t4, "Коммент 7", ProcessResult.POSITIVE, ProcessType.PAYMENT, null, user_itkostya, false, false, false),
                        new ExecutorTask(t3, businessProcessList.get(2), false, it_admin2, memorandumList.get(0), t4, "Коммент 8", ProcessResult.POSITIVE, ProcessType.PAYMENT, null, it_admin1, false, false, false),
                        new ExecutorTask(t3, businessProcessList.get(2), false, it_admin2, memorandumList.get(0), t4, "Коммент 9", ProcessResult.POSITIVE, ProcessType.PAYMENT, null, it_admin2, false, false, false),
                        new ExecutorTask(t1, businessProcessList.get(3), false, user_itkostya, message1, t2, "Коммент 11", ProcessResult.POSITIVE, ProcessType.INFORMATION, null, user_itkostya, false, false, false),
                        new ExecutorTask(t1, businessProcessList.get(3), false, user_itkostya, message1, t2, "Коммент 12", ProcessResult.POSITIVE, ProcessType.INFORMATION, null, it_admin1, false, false, false),
                        new ExecutorTask(t1, businessProcessList.get(3), false, user_itkostya, message1, t2, "Коммент 13", ProcessResult.POSITIVE, ProcessType.INFORMATION, null, it_admin2, false, false, false),
                        new ExecutorTask(t2, businessProcessList.get(4), false, it_admin1, message1, t3, "Коммент 14", ProcessResult.NEGATIVE, ProcessType.EXECUTION, null, user_itkostya, false, false, false),
                        new ExecutorTask(t2, businessProcessList.get(4), false, it_admin1, message1, t3, "Коммент 15", ProcessResult.COMPLETED, ProcessType.EXECUTION, null, it_admin1, false, false, false),
                        new ExecutorTask(t2, businessProcessList.get(4), false, it_admin1, message1, t3, "Коммент 16", ProcessResult.INFORMED, ProcessType.EXECUTION, null, it_admin2, false, false, false),
                        new ExecutorTask(t3, businessProcessList.get(5), false, it_admin2, message1, t4, "Коммент 17", ProcessResult.POSITIVE, ProcessType.PAYMENT, null, user_itkostya, false, false, false),
                        new ExecutorTask(t3, businessProcessList.get(5), false, it_admin2, message1, t4, "Коммент 18", ProcessResult.POSITIVE, ProcessType.PAYMENT, null, it_admin1, false, false, false),
                        new ExecutorTask(t3, businessProcessList.get(5), false, it_admin2, message1, t4, "Коммент 19", ProcessResult.POSITIVE, ProcessType.PAYMENT, null, it_admin2, false, false, false)));
        for (ExecutorTask executorTask : executorTaskList) {
            ExecutorTaskImpl.INSTANCE.save(executorTask);
            ExecutorTaskFolderStructureImpl.INSTANCE.save(new ExecutorTaskFolderStructure(FolderStructure.SENT, executorTask.getAuthor(), executorTask, false));
            ExecutorTaskFolderStructureImpl.INSTANCE.save(new ExecutorTaskFolderStructure(FolderStructure.INBOX, executorTask.getExecutor(), executorTask, false));
        }

        List<BusinessProcessSequence> businessProcessSequenceList = new ArrayList<>(
                Arrays.asList(
                        new BusinessProcessSequence(t1, businessProcessList.get(0), user_itkostya, false, ProcessOrderType.AFTER, ProcessResult.ACCOMMODATED, ProcessType.EXECUTION, false, executorTaskList.get(0)),
                        new BusinessProcessSequence(t1, businessProcessList.get(0), it_admin1, false, ProcessOrderType.TOGETHER, ProcessResult.CANCELED, ProcessType.INFORMATION, false, null),
                        new BusinessProcessSequence(t1, businessProcessList.get(0), it_admin2, false, ProcessOrderType.TOGETHER, ProcessResult.POSITIVE, ProcessType.VISA, false, null),
                        new BusinessProcessSequence(t2, businessProcessList.get(1), user_itkostya, false, ProcessOrderType.AFTER, ProcessResult.ACCOMMODATED, ProcessType.EXECUTION, false, executorTaskList.get(3)),
                        new BusinessProcessSequence(t2, businessProcessList.get(1), it_admin1, false, ProcessOrderType.TOGETHER, ProcessResult.CANCELED, ProcessType.INFORMATION, false, null),
                        new BusinessProcessSequence(t2, businessProcessList.get(1), it_admin2, false, ProcessOrderType.TOGETHER, ProcessResult.POSITIVE, ProcessType.VISA, false, null),
                        new BusinessProcessSequence(t3, businessProcessList.get(2), user_itkostya, false, ProcessOrderType.AFTER, ProcessResult.ACCOMMODATED, ProcessType.EXECUTION, false, executorTaskList.get(6)),
                        new BusinessProcessSequence(t3, businessProcessList.get(2), it_admin1, false, ProcessOrderType.TOGETHER, ProcessResult.CANCELED, ProcessType.INFORMATION, false, null),
                        new BusinessProcessSequence(t3, businessProcessList.get(2), it_admin2, false, ProcessOrderType.TOGETHER, ProcessResult.POSITIVE, ProcessType.VISA, false, null)));
        for (BusinessProcessSequence businessProcessSequence : businessProcessSequenceList)
            BusinessProcessSequenceImpl.INSTANCE.save(businessProcessSequence);


        createCheckFileFolder();
    }

    public static void createCheckFileFolder(){

        final String folderName = System.getProperty("catalina.home");

        File dir = new File(System.getProperty("catalina.home")+ File.separator+"files");
        boolean created = dir.mkdir();
        if(created)
            System.out.println("Folder created");

    }

}
