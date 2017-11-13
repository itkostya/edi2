package categories;

import abstract_entity.AbstractCategory;

import javax.persistence.Entity;
import javax.persistence.Table;

/*
* Our official companies.
* I don't want to name it "LegalEntity" (common phrase) because "entity" uses frequently in java
*
* Created by kostya on 11/13/2017.
*/

@Entity
@Table(name = "CAT_LEGAL_ORGANIZATION")
public class LegalOrganization extends AbstractCategory { // Юр. лицо

    public LegalOrganization() {
        super();
    }

    public LegalOrganization(String name) {
        super(name);
    }
}
