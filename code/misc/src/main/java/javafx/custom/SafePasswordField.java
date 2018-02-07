package javafx.custom;

import java.lang.reflect.Field;

import javafx.scene.control.PasswordField;

public class SafePasswordField extends PasswordField {
  public final char[] getPassword() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    Content c = getContent();

    StringBuilder sb = (StringBuilder) getFieldReflection(c, c.getClass(), "characters");
    char[] result = (char[]) getFieldReflection(sb, sb.getClass().getSuperclass(), "value");

    return result;
  }

  private Object getFieldReflection(Object o, Class<?> c, String field)
      throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    Field f = c.getDeclaredField(field);
    f.setAccessible(true);
    return f.get(o);
  }
}
