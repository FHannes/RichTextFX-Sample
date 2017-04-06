package textwrapper;

import javafx.scene.Node;

public class EmptyTextObj<S> implements TextObj<S> {

    @Override
    public TextObj<S> setStyle(S style) {
        return null;
    }

    @Override
    public S getStyle() {
        return null;
    }

    @Override
    public String getReference() {
        return "";
    }

    @Override
    public Node createNode() {
        return null;
    }

}
