package protocol;
public abstract class JSON {
  public abstract String toString();
}

public interface PandaSoxSerializable {
  public JSON serialize();
}

