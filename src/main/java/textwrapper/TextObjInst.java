package textwrapper;

import javafx.scene.Node;

public class TextObjInst<S> implements TextObj<S> {

    private final String reference;
    private final S style;

    public TextObjInst(String reference, S style) {
        this.reference = reference;
        this.style = style;
    }

    @Override
    public TextObjInst<S> setStyle(S style) {
        return new TextObjInst<>(reference, style);
    }

    @Override
    public S getStyle() {
        return style;
    }

    @Override
    public String getReference() {
        return reference;
    }

    @Override
    public Node createNode() {
        return new TextObjNode(reference);
    }

    @Override
    public String toString() {
        return String.format("TextObj[reference=%s]", reference);
    }

}
