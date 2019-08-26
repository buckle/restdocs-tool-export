package restdocs.tool.export.common.creator;

public interface Creator<T, E> {

  T create(E source);

}
