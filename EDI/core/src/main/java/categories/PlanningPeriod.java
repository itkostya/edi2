package categories;

import abstract_entity.AbstractCategory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;
import java.util.Objects;

/*
* PlanningPeriod for planning
*
* Created by kostya on 11/13/2017.
*/

@Entity
@Table(name = "CAT_PLANNING_PERIOD")
public class PlanningPeriod extends AbstractCategory {

    @Column(name = "begin_of_period")
    private Date beginOfPeriod;

    @Column(name = "end_of_period")
    private Date endOfPeriod;

    public PlanningPeriod() {
        super();
    }

    public PlanningPeriod(String name, Date beginOfPeriod, Date endOfPeriod) {
        super(name);
        this.beginOfPeriod = beginOfPeriod;
        this.endOfPeriod = endOfPeriod;
    }

    public Date getBeginOfPeriod() {
        return beginOfPeriod;
    }

    public void setBeginOfPeriod(Date beginOfPeriod) {
        this.beginOfPeriod = beginOfPeriod;
    }

    public Date getEndOfPeriod() {
        return endOfPeriod;
    }

    public void setEndOfPeriod(Date endOfPeriod) {
        this.endOfPeriod = endOfPeriod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PlanningPeriod periods = (PlanningPeriod) o;
        return Objects.equals(beginOfPeriod, periods.beginOfPeriod) &&
                Objects.equals(endOfPeriod, periods.endOfPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), beginOfPeriod, endOfPeriod);
    }
}
