package categories;

import abstract_entity.AbstractCategory;

import javax.persistence.Entity;
import javax.persistence.Table;

/*
* Analytics.
* Enlarged items: raw material, transport, utilities, etc.
* For what we paid money
*
* Created by kostya on 11/13/2017.
*/

@Entity
@Table(name = "CAT_COST_ITEM")
public class CostItem extends AbstractCategory {  // Статья затрат

    public CostItem() {
        super();
    }

    public CostItem(String name) {
        super(name);
    }

}
