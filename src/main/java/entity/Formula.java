package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "formulas")
public class Formula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String expression;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "formula_roots", joinColumns = @JoinColumn(name = "formula_id"))
    @Column(name = "root_value")
    private List<Double> root;

    public Formula() {
        root = new ArrayList<>();
    }

    public Formula(String expression, List<Double> root) {
        this.expression = expression;
        this.root = root;
    }

    // Getter and setter methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormula() {
        return expression;
    }

    public void setFormula(String expression) {
        this.expression = expression;
    }

    public List<Double> getRoot() {
        return root;
    }

    public void setRoot(List<Double> root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return "Formula{" +
                "expression='" + expression + '\'' +
                ", roots=" + root.stream()
                .toList() +
                '}';
    }
}
