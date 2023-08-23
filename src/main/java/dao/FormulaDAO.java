package dao;

import entity.Formula;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FormulaDAO {

    private static FormulaDAO instance;
    private List<Formula> cachedFormulas;

    private FormulaDAO() {
        cachedFormulas = getFormulas(true);
    }

    public static FormulaDAO getInstance() {
        if (instance == null) {
            instance = new FormulaDAO();
        }
        return instance;
    }

    private static Session getSession() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    public void saveFormula(Formula expression) {

        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();

            session.save(expression);
            cachedFormulas.add(expression);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Saving failed. Error: " + e.getMessage());
        }
    }

    public List<Formula> getFormulas(boolean fetchFromDB) {
        if (fetchFromDB) {
            try (Session session = getSession()) {
                Query<Formula> query = session.createQuery("FROM Formula", Formula.class);
                cachedFormulas = query.getResultList();
            } catch (Exception e) {
                System.out.println("Getting all formulas has failed. Error: " + e.getMessage());

            }
        }
        return cachedFormulas;
    }

    public Formula getFormulaByExpression(String expression) {
        Formula formula = null;

        Optional<Formula> optionalFormula = cachedFormulas.stream()
                .filter(f -> f.getFormula().equals(expression))
                .findFirst();

        if (optionalFormula.isPresent()) {
            formula = optionalFormula.get();
        } else {
            try (Session session = getSession()) {
                Query<Formula> query = session.createQuery("FROM Formula WHERE expression = :expression", Formula.class);
                query.setParameter("expression", expression);
                formula = query.uniqueResult();
            } catch (Exception e) {
                System.out.println("Getting formula by expression has failed. Error: " + e.getMessage());
            }
        }
        return formula;
    }

    public List<Formula> getFormulasByRoot(Double root) {

        return getFormulas(false).stream()
                .filter(formula -> formula.getRoot().contains(root))
                .collect(Collectors.toList());
    }
}
