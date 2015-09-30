package at.bestsolution.sample.code.app.handler;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.fx.ui.services.theme.ThemeManager;

public class SwitchTheme {
	@Execute
	public void switchTheme(ThemeManager m, @Named("theme.id") String themeId) {
		m.setCurrentThemeId(themeId);
	}
}
