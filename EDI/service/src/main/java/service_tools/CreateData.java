package service_tools;

import business_processes.BusinessProcess;
import business_processes.BusinessProcessSequence;
import business_processes.ExecutorTask;
import business_processes.ExecutorTaskFolderStructure;
import categories.*;
import documents.DocumentProperty;
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
import hibernate.impl.categories.AbstractCategoryImpl;
import hibernate.impl.categories.DepartmentImpl;
import hibernate.impl.categories.PositionImpl;
import hibernate.impl.categories.UserImpl;
import hibernate.impl.documents.MemorandumImpl;
import hibernate.impl.documents.MessageImpl;
import impl.information_registers.UserAccessRightServiceImpl;

import java.io.File;
import java.sql.Date;
import java.sql.SQLData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Created by kostya on 9/2/2016.
 */
public enum CreateData {

    @SuppressWarnings("unused")
    INSTANCE;

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
        User user;
        user = new User("admin", false, null, false, "Gates", "William", "Henry", "admin", "MS", "12345", null, null, UserRole.ADMIN);
        UserImpl.INSTANCE.save(user);
        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);

        user = new User("kostya", false, null, false, "Zhurov", "Kostyantin", "Oleksandrovich", "kostya", "DC", null, null, null, null);
        UserImpl.INSTANCE.save(user);
        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);

        user = new User("itkostya", false, null, false, "Журов", "Константин", "Александрович", "itkostya", null, "123", positionProgrammer, departmentIt, UserRole.USER);
        UserImpl.INSTANCE.save(user);
        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);

        user = new User("it_director", false, null, false, "Чегалкин", "Сергей", "Викторович", "it_director", null, "123", positionItDirector, departmentIt, UserRole.USER);
        UserImpl.INSTANCE.save(user);
        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);

        user = new User("it_admin1", false, null, false, "Капитошка", "Виталий", "Владимирович", "it_admin1", null, "123", positionAdmin, departmentIt, UserRole.USER);
        UserImpl.INSTANCE.save(user);
        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);

        user = new User("it_admin2", false, null, false, "Рябчиков", "Сергей", "Владимирович", "it_admin2", null, "123", positionAdmin, departmentIt, UserRole.USER);
        UserImpl.INSTANCE.save(user);
        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);

        user = new User("hr_director", false, null, false, "Drocenko", "Ekaterina", "Valerievna", "hr_director", null, "234", positionHrDirector, departmentHr, UserRole.USER);
        UserImpl.INSTANCE.save(user);
        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);

        user = new User("hr_manager1", false, null, false, "Kruglenko", "Olga", "Aleksandrovna", "hr_manager1", null, "234", positionHrManager, departmentHr, UserRole.USER);
        UserImpl.INSTANCE.save(user);
        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);

        user = new User("hr_manager2", false, null, false, "Karpova", "Julia", "Aleksandrovna", "hr_manager2", null, "234", positionHrManager, departmentHr, UserRole.USER);
        UserImpl.INSTANCE.save(user);
        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);

        AbstractCategoryImpl.INSTANCE.save(new Contractor("Сильпо-Фуд ООО", false, null, false, "407201926538", "407201926538"));
        AbstractCategoryImpl.INSTANCE.save(new Contractor("РТЦ Варус-8 ТОВ (Днепр)", false, null, false, "33184262", "331842604637"));
        AbstractCategoryImpl.INSTANCE.save(new Contractor("АТБ маркет", false, null, false, "30487219", "304872104175"));

        AbstractCategoryImpl.INSTANCE.save(new CostItem("Коммуналка"));
        AbstractCategoryImpl.INSTANCE.save(new CostItem("Маркетинг и реклама"));
        AbstractCategoryImpl.INSTANCE.save(new CostItem("Затраты на персонал"));

        AbstractCategoryImpl.INSTANCE.save(new Currency("USD", false, 840L, false));
        AbstractCategoryImpl.INSTANCE.save(new Currency("EUR", false, 978L, false));
        AbstractCategoryImpl.INSTANCE.save(new Currency("UAH", false, 980L, false));

        AbstractCategoryImpl.INSTANCE.save(new LegalOrganization("АКВАМИНЕРАЛЕ ООО"));
        AbstractCategoryImpl.INSTANCE.save(new LegalOrganization("Национальный продукт ООО"));
        AbstractCategoryImpl.INSTANCE.save(new LegalOrganization("Чегалкин ЧП"));

        AbstractCategoryImpl.INSTANCE.save(new PlanningPeriod("Ноябрь 2017", Date.valueOf("2017-11-01"), Date.valueOf("2017-11-30") ));
        AbstractCategoryImpl.INSTANCE.save(new PlanningPeriod("Декабрь 2017", Date.valueOf("2017-12-01"), Date.valueOf("2017-12-31")));
        AbstractCategoryImpl.INSTANCE.save(new PlanningPeriod("Январь 2018", Date.valueOf("2018-01-01"), Date.valueOf("2018-01-31")));

    }

 }
