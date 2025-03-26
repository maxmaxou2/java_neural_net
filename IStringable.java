public interface IStringable {
    public String serialize();
    public IStringable deserialize(String serialized);
}
