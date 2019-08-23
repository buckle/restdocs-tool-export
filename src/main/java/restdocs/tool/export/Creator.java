package restdocs.tool.export;

public interface Creator<T, E> {

  T create(E source);

}
