package com.bstek.dorado.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;

import com.bstek.dorado.data.ParameterWrapper;
import com.bstek.dorado.data.provider.Or;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.data.provider.filter.PropertyFilterCriterion;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;
import com.bstek.dorado.data.variant.Record;

public final class HibernateUtils {
    private HibernateUtils() {
    }

    /**
     * 根据属性路径获取在hibernate中的Type
     *
     * @param property
     * @param classMetadata
     * @param sessionFactory
     * @return
     */
    public static Type getHibernateType(String property, ClassMetadata classMetadata, SessionFactory sessionFactory) {
        String[] tokens = StringUtils.split(property, '.');
        if (tokens.length == 1) {
            return classMetadata.getPropertyType(property);
        } else if (tokens.length > 1) {
            Type type = null;
            ClassMetadata meta = classMetadata;
            for (String token : tokens) {
                type = meta.getPropertyType(token);
                if (type instanceof EntityType) {
                    EntityType entityType = (EntityType) type;
                    String entityName = entityType.getAssociatedEntityName();
                    meta = sessionFactory.getClassMetadata(entityName);
                }
            }
            return type;
        }
        return null;
    }

    public static void makeCount(Criteria entityCriteria) throws Exception {
        CriteriaImplHelper helper = new CriteriaImplHelper(entityCriteria);
        helper.makeCount();
    }

    public static com.bstek.dorado.data.provider.Criteria getFilterCriteria(
            Object parameter) {
        if (parameter instanceof ParameterWrapper) {
            ParameterWrapper pw = (ParameterWrapper) parameter;
            Map<String, Object> sysParameter = pw.getSysParameter();

            if (sysParameter instanceof Record) {
                Record paraRecord = (Record) sysParameter;
                return (com.bstek.dorado.data.provider.Criteria) paraRecord
                        .get("criteria");
            }
        }

        return null;
    }

    public static Object getRealParameter(Object parameter) {
        if (parameter != null) {
            if (parameter instanceof ParameterWrapper) {
                return ((ParameterWrapper) parameter).getParameter();
            }
        }

        return parameter;
    }

    public static DetachedCriteria createFilter(
            DetachedCriteria detachedCriteria,
            com.bstek.dorado.data.provider.Criteria filterCriteria)
            throws Exception {
        CriteriaImplHelper helper = new CriteriaImplHelper(detachedCriteria);
        mergeFilter(helper, filterCriteria);
        return detachedCriteria;
    }

    public static void mergeFilter(CriteriaImplHelper helper,
                                   com.bstek.dorado.data.provider.Criteria filterCriteria)
            throws Exception {
        org.hibernate.Criteria entityCriteria = helper.getCriteria();

        List<com.bstek.dorado.data.provider.Criterion> filterCriterions = filterCriteria
                .getCriterions();
        for (com.bstek.dorado.data.provider.Criterion fCriterion : filterCriterions) {
            Criterion criterion = createCriterion(helper, fCriterion);
            entityCriteria.add(criterion);
        }

        List<com.bstek.dorado.data.provider.Order> filterOrders = filterCriteria
                .getOrders();
        List<org.hibernate.criterion.Order> hOrders = new ArrayList<org.hibernate.criterion.Order>();

        for (com.bstek.dorado.data.provider.Order fOrder : filterOrders) {
            String property = fOrder.getProperty();
            String criteriaPath = helper.getCriteriaPath(StringUtils
                    .defaultIfEmpty(fOrder.getPropertyPath(), property));

            org.hibernate.criterion.Order hOrder = fOrder.isDesc() ? org.hibernate.criterion.Order
                    .desc(criteriaPath) : org.hibernate.criterion.Order
                    .asc(criteriaPath);

            hOrders.add(hOrder);
        }

        if (!hOrders.isEmpty()) {
            helper.mergeOrders(hOrders);
        }
    }

    private static org.hibernate.criterion.Criterion createCriterion(
            CriteriaImplHelper helper,
            com.bstek.dorado.data.provider.Criterion fCriterion) throws Exception {
        org.hibernate.criterion.Criterion criterion = null;
        if (fCriterion instanceof SingleValueFilterCriterion) {
            SingleValueFilterCriterion filterCriterion = (SingleValueFilterCriterion) fCriterion;
            FilterOperator filterOperator = filterCriterion.getFilterOperator();
            Object filterValue = filterCriterion.getValue();
            String property = filterCriterion.getProperty();
            String criteriaPath = helper
                    .getCriteriaPath(StringUtils.defaultIfEmpty(
                            filterCriterion.getPropertyPath(), property));

            if (FilterOperator.eq.equals(filterOperator)) {
                criterion = Restrictions.eq(criteriaPath, filterValue);
            } else if (FilterOperator.ne.equals(filterOperator)) {
                criterion = Restrictions.ne(criteriaPath, filterValue);
            } else if (FilterOperator.gt.equals(filterOperator)) {
                criterion = Restrictions.gt(criteriaPath, filterValue);
            } else if (FilterOperator.lt.equals(filterOperator)) {
                criterion = Restrictions.lt(criteriaPath, filterValue);
            } else if (FilterOperator.ge.equals(filterOperator)) {
                criterion = Restrictions.ge(criteriaPath, filterValue);
            } else if (FilterOperator.le.equals(filterOperator)) {
                criterion = Restrictions.le(criteriaPath, filterValue);
            } else if (FilterOperator.like.equals(filterOperator)) {
                if (filterValue != null) {
                    String strValue = String.valueOf(filterValue);
                    if (StringUtils.isNotEmpty(strValue)) {
                        criterion = Restrictions.like(criteriaPath, strValue,
                                MatchMode.ANYWHERE);
                    }
                }
            } else if (FilterOperator.likeStart.equals(filterOperator)) {
                if (filterValue != null) {
                    String strValue = String.valueOf(filterValue);
                    if (StringUtils.isNotEmpty(strValue)) {
                        criterion = Restrictions.like(criteriaPath, strValue,
                                MatchMode.START);
                    }
                }
            } else if (FilterOperator.likeEnd.equals(filterOperator)) {
                if (filterValue != null) {
                    String strValue = String.valueOf(filterValue);
                    if (StringUtils.isNotEmpty(strValue)) {
                        criterion = Restrictions.like(criteriaPath, strValue,
                                MatchMode.END);
                    }
                }
            } else if (FilterOperator.between.equals(filterOperator)) {
                Object[] values = (Object[]) filterValue;
                criterion = Restrictions.between(criteriaPath, values[0],
                        values[1]);
            } else if (FilterOperator.in.equals(filterOperator)) {
                Object[] values = (Object[]) filterValue;
                criterion = Restrictions.in(criteriaPath, values);
            } else {
                throw new IllegalArgumentException(
                        "Unsupported FilterOperator [" + filterOperator + "]");
            }
        } else if (fCriterion instanceof PropertyFilterCriterion) {
            PropertyFilterCriterion filterCriterion = (PropertyFilterCriterion) fCriterion;
            FilterOperator filterOperator = filterCriterion.getFilterOperator();
            String property = filterCriterion.getProperty();
            String otherProperty = filterCriterion.getOtherProperty();
            String criteriaPath = helper
                    .getCriteriaPath(StringUtils.defaultIfEmpty(
                            filterCriterion.getPropertyPath(), property));
            String otherCriteriaPath = helper.getCriteriaPath(StringUtils
                    .defaultIfEmpty(filterCriterion.getOtherPropertyPath(),
                            otherProperty));

            if (FilterOperator.eq.equals(filterOperator)) {
                criterion = Restrictions.eqProperty(criteriaPath,
                        otherCriteriaPath);
            } else if (FilterOperator.ne.equals(filterOperator)) {
                criterion = Restrictions.neProperty(criteriaPath,
                        otherCriteriaPath);
            } else if (FilterOperator.gt.equals(filterOperator)) {
                criterion = Restrictions.gtProperty(criteriaPath,
                        otherCriteriaPath);
            } else if (FilterOperator.lt.equals(filterOperator)) {
                criterion = Restrictions.ltProperty(criteriaPath,
                        otherCriteriaPath);
            } else if (FilterOperator.ge.equals(filterOperator)) {
                criterion = Restrictions.geProperty(criteriaPath,
                        otherCriteriaPath);
            } else if (FilterOperator.le.equals(filterOperator)) {
                criterion = Restrictions.leProperty(criteriaPath,
                        otherCriteriaPath);
            } else {
                throw new IllegalArgumentException(
                        "Unsupported FilterOperator [" + filterOperator + "]");
            }
        } else if (fCriterion instanceof com.bstek.dorado.data.provider.Junction) {
            com.bstek.dorado.data.provider.Junction fJunction = (com.bstek.dorado.data.provider.Junction) fCriterion;
            if (!fJunction.getCriterions().isEmpty()) {
                Junction junction;
                if (fCriterion instanceof Or) {
                    junction = Restrictions.disjunction();
                } else {
                    junction = Restrictions.conjunction();
                }
                criterion = junction;

                for (com.bstek.dorado.data.provider.Criterion c : fJunction
                        .getCriterions()) {
                    Criterion subCriterion = createCriterion(helper, c);
                    junction.add(subCriterion);
                }
            }
        } else {
            throw new IllegalArgumentException("Unsupported Criterion ["
                    + fCriterion + "]");
        }
        return criterion;
    }
}
