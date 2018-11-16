package categories;

import abstract_entity.AbstractCategory;
import abstract_entity.AbstractDocumentEdi;
import business_processes.BusinessProcess;
import business_processes.BusinessProcessSequence;
import business_processes.ExecutorTask;
import business_processes.ExecutorTaskFolderStructure;
import documents.Memorandum;
import information_registers.UserAccessRight;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/*
* Information about users
*
*/

@Entity
@Table(schema = "EDI",
        name = "CAT_USER",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"login", "domain"})}
        )
public class User extends AbstractCategory{

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "login")
    private String login;

    @Column(name = "domain")
    private String domain;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.ORDINAL)
    private UserRole role;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Formula(value = "CONCAT(last_name,' ',first_name,' ',middle_name)")
    private String fio;

    @OneToMany(mappedBy = "author")
    private Set<AbstractDocumentEdi> documentSet = new HashSet<>();

    @OneToMany(mappedBy = "whom")
    private Set<Memorandum> documentSetWhom = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<BusinessProcess> businessProcessSet = new HashSet<>();

    @OneToMany(mappedBy = "executor")
    private Set<BusinessProcessSequence> businessProcessSequenceSet = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<ExecutorTask> executorTaskAuthorSet = new HashSet<>();

    @OneToMany(mappedBy = "executor")
    private Set<ExecutorTask> executorTaskExecutorSet = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<ExecutorTaskFolderStructure> executorTaskFolderStructureUserSet = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserAccessRight> userAccessRightSet = new HashSet<>();

    public User() {
    }

    public User(String name) {
        super(name);
    }

    public User(String name, boolean deletionMark, Long code, boolean isFolder) {
        super(name, deletionMark, code, isFolder);
    }

    public User(String name, boolean deletionMark, Long code, boolean isFolder, String lastName, String firstName, String middleName, String login, String domain, String password, Position position, Department department, UserRole role) {
        super(name, deletionMark, code, isFolder);
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.login = login;
        this.domain = domain;
        this.password = password;
        this.position = position;
        this.department = department;
        this.role = role;
    }

    public String getFioInitials(){
        return ""+lastName + " "+ (firstName.equals("") ? "": firstName.charAt(0)+". ")+ (middleName.equals("") ? "": middleName.charAt(0)+". ");
    }

    public String getLastName() {
        return lastName;
    }

    @SuppressWarnings("unused")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    @SuppressWarnings("unused")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    @SuppressWarnings("unused")
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @SuppressWarnings("unused")
    public String getDomain() {
        return domain;
    }

    @SuppressWarnings("unused")
    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Position getPosition() {
        return position;
    }

    @SuppressWarnings("unused")
    public void setPosition(Position position) {
        this.position = position;
    }

    public Department getDepartment() {
        return department;
    }

    @SuppressWarnings("unused")
    public void setDepartment(Department department) {
        this.department = department;
    }

    public UserRole getRole() {
        return role;
    }

    @SuppressWarnings("unused")
    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getFio() {
        return fio;
    }

    @SuppressWarnings("unused")
    public void setFio(String fio) {
        this.fio = fio;
    }

    @SuppressWarnings("unused")
    public Set<AbstractDocumentEdi> getDocumentSet() {
        return documentSet;
    }

    @SuppressWarnings("unused")
    public void setDocumentSet(Set<AbstractDocumentEdi> documentSet) {
        this.documentSet = documentSet;
    }

    @SuppressWarnings("unused")
    public Set<Memorandum> getDocumentSetWhom() {
        return documentSetWhom;
    }

    @SuppressWarnings("unused")
    public void setDocumentSetWhom(Set<Memorandum> documentSetWhom) {
        this.documentSetWhom = documentSetWhom;
    }

    @SuppressWarnings("unused")
    public Set<BusinessProcess> getBusinessProcessSet() {
        return businessProcessSet;
    }

    @SuppressWarnings("unused")
    public void setBusinessProcessSet(Set<BusinessProcess> businessProcessSet) {
        this.businessProcessSet = businessProcessSet;
    }

    @SuppressWarnings("unused")
    public Set<BusinessProcessSequence> getBusinessProcessSequence() {
        return businessProcessSequenceSet;
    }

    @SuppressWarnings("unused")
    public void setBusinessProcessSequence(Set<BusinessProcessSequence> businessProcessSequenceSet) {
        this.businessProcessSequenceSet = businessProcessSequenceSet;
    }

    @SuppressWarnings("unused")
    public Set<BusinessProcessSequence> getBusinessProcessSequenceSet() {
        return businessProcessSequenceSet;
    }

    @SuppressWarnings("unused")
    public void setBusinessProcessSequenceSet(Set<BusinessProcessSequence> businessProcessSequenceSet) {
        this.businessProcessSequenceSet = businessProcessSequenceSet;
    }

    @SuppressWarnings("unused")
    public Set<ExecutorTask> getExecutorTaskAuthorSet() {
        return executorTaskAuthorSet;
    }

    @SuppressWarnings("unused")
    public void setExecutorTaskAuthorSet(Set<ExecutorTask> executorTaskAuthorSet) {
        this.executorTaskAuthorSet = executorTaskAuthorSet;
    }

    @SuppressWarnings("unused")
    public Set<ExecutorTask> getExecutorTaskExecutorSet() {
        return executorTaskExecutorSet;
    }

    @SuppressWarnings("unused")
    public void setExecutorTaskExecutorSet(Set<ExecutorTask> executorTaskExecutorSet) {
        this.executorTaskExecutorSet = executorTaskExecutorSet;
    }

    @SuppressWarnings("unused")
    public Set<ExecutorTaskFolderStructure> getExecutorTaskFolderStructureUserSet() {
        return executorTaskFolderStructureUserSet;
    }

    @SuppressWarnings("unused")
    public void setExecutorTaskFolderStructureUserSet(Set<ExecutorTaskFolderStructure> executorTaskFolderStructureUserSet) {
        this.executorTaskFolderStructureUserSet = executorTaskFolderStructureUserSet;
    }

    @SuppressWarnings("unused")
    public Set<UserAccessRight> getUserAccessRightSet() {
        return userAccessRightSet;
    }

    @SuppressWarnings("unused")
    public void setUserAccessRightSet(Set<UserAccessRight> userAccessRightSet) {
        this.userAccessRightSet = userAccessRightSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(lastName, user.lastName) && Objects.equals(firstName, user.firstName) && Objects.equals(middleName, user.middleName) && Objects.equals(login, user.login) && Objects.equals(domain, user.domain) && Objects.equals(password, user.password) && Objects.equals(position, user.position) && Objects.equals(department, user.department) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lastName, firstName, middleName, login, domain, password, position, department, role);
    }
}
