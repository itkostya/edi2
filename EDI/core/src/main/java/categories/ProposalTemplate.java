package categories;

import abstract_entity.AbstractCategory;

import javax.persistence.*;

/*
* Proposal templates. It will be used in new document PaymentProposal -
* help for filling document
*
* Created by kostya on 10/26/2017.
*/

@Entity
@Table(schema = "EDI", name = "CAT_PROPOSAL_TEMPLATE")
public class ProposalTemplate extends AbstractCategory {

    @Column(name = "rationale")
    private String rationale;   // Обоснование

    @ManyToOne
    @JoinColumn(name = "departmentResponsible_id")
    private Department departmentResponsible;   // Who create payment proposal

    @ManyToOne
    @JoinColumn(name = "departmentDestination_id")
    private Department departmentDestination;   // Who get payment proposal

    public ProposalTemplate() {
    }

    public String getRationale() {
        return rationale;
    }

    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    public Department getDepartmentResponsible() {
        return departmentResponsible;
    }

    public void setDepartmentResponsible(Department departmentResponsible) {
        this.departmentResponsible = departmentResponsible;
    }

    public Department getDepartmentDestination() {
        return departmentDestination;
    }

    public void setDepartmentDestination(Department departmentDestination) {
        this.departmentDestination = departmentDestination;
    }
}
