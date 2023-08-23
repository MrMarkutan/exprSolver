package my.project;

import dao.FormulaDAO;
import entity.Formula;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AppTest {

    @Mock
    public static FormulaDAO formulaDAO;
    public static Formula testFormula;

    @BeforeAll
    static void beforeAll() {
        formulaDAO = FormulaDAO.getInstance();

        String expression = "2*x+5=17";
        List<Double> roots = List.of(6.0);
        testFormula = new Formula();
        testFormula.setFormula(expression);
        testFormula.setRoot(roots);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getFormulaByExpression() {
        when(formulaDAO.getFormulaByExpression(anyString())).thenReturn(testFormula);

        Formula retrievedFormula = formulaDAO.getFormulaByExpression("2*x+5=17");

        verify(formulaDAO, times(1)).getFormulaByExpression(anyString());

        assertEquals(testFormula.getFormula(), retrievedFormula.getFormula());
        assertEquals(testFormula.getRoot().size(), retrievedFormula.getRoot().size());
        assertEquals(testFormula.getRoot().get(0), retrievedFormula.getRoot().get(0));
    }

    @Test
    void getFormulasByRoot() {
        List<Formula> fakeList = List.of(testFormula);

        when(formulaDAO.getFormulasByRoot(anyDouble())).thenReturn(fakeList);

        List<Formula> formulasByRoot = formulaDAO.getFormulasByRoot(anyDouble());

        verify(formulaDAO, times(1)).getFormulasByRoot(anyDouble());
        assertEquals(fakeList.size(), formulasByRoot.size());
        assertEquals(fakeList.get(0), formulasByRoot.get(0));
        assertEquals(fakeList.get(0).getFormula(), formulasByRoot.get(0).getFormula());
        assertEquals(fakeList.get(0).getRoot().size(), formulasByRoot.get(0).getRoot().size());
        assertEquals(fakeList.get(0).getRoot().get(0), formulasByRoot.get(0).getRoot().get(0));
    }

    @Test
    void getFormulas() {
        List<Formula> fakeList = List.of(testFormula);
        when(formulaDAO.getFormulas(anyBoolean())).thenReturn(fakeList);

        List<Formula> formulasFromDB = formulaDAO.getFormulas(true);

        verify(formulaDAO, times(1)).getFormulas(anyBoolean());
        assertEquals(fakeList.size(), formulasFromDB.size());
        assertEquals(fakeList.get(0), formulasFromDB.get(0));
        assertEquals(fakeList.get(0).getFormula(), formulasFromDB.get(0).getFormula());
        assertEquals(fakeList.get(0).getRoot().size(), formulasFromDB.get(0).getRoot().size());
        assertEquals(fakeList.get(0).getRoot().get(0), formulasFromDB.get(0).getRoot().get(0));

    }

    @Test
    void saveFormula() {
        doNothing().when(formulaDAO).saveFormula(any(Formula.class));

        formulaDAO.saveFormula(testFormula);

        verify(formulaDAO, times(1)).saveFormula(any(Formula.class));
    }

}