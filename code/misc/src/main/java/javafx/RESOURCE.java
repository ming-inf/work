package javafx;

public enum RESOURCE {
  GREETING("greeting"), PASSWORD_PROMPT("passwordPrompt"), PASSWORD_LABEL("passwordLabel"), ROOT_LABEL("rootLabel");

  private String key;

  private RESOURCE(String key) {
    this.key = key;
  }

  public String toString() {
    return key;
  }
}
