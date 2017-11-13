package categories;

public enum CategoryProperty {

    CONTRACTOR("Contractor", "Contractors", "Контрагент", "Контрагенты", "Справочник - Контрагент",  "Справочник - Контрагенты"),
    COST_ITEM("Cost item", "Cost items", "Статья затрат", "Статьи затрат", "Справочник - Статья затрат",  "Справочник - статьи затрат"),
    CURRENCY("Currency", "Currencies", "Валюта", "Валюты", "Справочник - Валюта",  "Справочник - Валюты"),
    DEPARTMENT("Department", "Departments", "Департамент", "Департаменты", "Справочник - Департамент",  "Справочник - Департаменты"),
    LEGAL_ORGANIZATION("Legal organization", "Legal organizations", "Юридическое лицо", "Юридические лица", "Справочник - Юридическое лицо",  "Справочник - Юридические лица"),
    PLANNING_PERIOD("Period of planning", "Periods of planning", "Период планирования", "Периоды планирования", "Справочник - Период планирования",  "Справочник - Периоды планирования"),
    POSITION("Position", "Positions", "Должность", "Должности", "Справочник - Должность",  "Справочник - Должности"),
    PROPOSAL_TEMPLATE("Proposal template", "Proposal templates", "Шаблон заявок", "Шаблоны заявок", "Справочник - Шаблон заявок", "Справочник - Шаблоны заявок"),
    UPLOADED_FILE("Uploaded file", "Uploaded files", "Файл", "Файлы", "Файл", "Файлы"),
    USER("User", "Users", "Пользователь", "Пользователи", "Справочник - Пользователь", "Справочник - Пользователи");

    private String enName;
    private String enPluralName;
    private String ruShortName;
    private String ruPluralShortName;
    private String ruFullName;
    private String ruPluralFullName;

    CategoryProperty() {
    }

    CategoryProperty(String enName, String enPluralName, String ruShortName, String ruPluralShortName, String ruFullName, String ruPluralFullName) {
        this.enName = enName;
        this.enPluralName = enPluralName;
        this.ruShortName = ruShortName;
        this.ruPluralShortName = ruPluralShortName;
        this.ruFullName = ruFullName;
        this.ruPluralFullName = ruPluralFullName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getEnPluralName() {
        return enPluralName;
    }

    public void setEnPluralName(String enPluralName) {
        this.enPluralName = enPluralName;
    }

    public String getRuShortName() {
        return ruShortName;
    }

    public void setRuShortName(String ruShortName) {
        this.ruShortName = ruShortName;
    }

    public String getRuPluralShortName() {
        return ruPluralShortName;
    }

    public void setRuPluralShortName(String ruPluralShortName) {
        this.ruPluralShortName = ruPluralShortName;
    }

    public String getRuFullName() {
        return ruFullName;
    }

    public void setRuFullName(String ruFullName) {
        this.ruFullName = ruFullName;
    }

    public String getRuPluralFullName() {
        return ruPluralFullName;
    }

    public void setRuPluralFullName(String ruPluralFullName) {
        this.ruPluralFullName = ruPluralFullName;
    }

}
