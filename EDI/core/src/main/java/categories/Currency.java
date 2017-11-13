package categories;

import abstract_entity.AbstractCategory;

import javax.persistence.Entity;
import javax.persistence.Table;

/*
* Different currencies: USD, EUR, UAH, etc.
* not enum cause admin can add new currency without stopping the application
*
* Created by kostya on 11/13/2017.
*/

@Entity
@Table(name = "CAT_CURRENCY")
public class Currency extends AbstractCategory {

    public Currency() {
        super();
    }

    public Currency(String name, boolean deletionMark, Long code, boolean isFolder) {
        super(name, deletionMark, code, isFolder);
    }
}
