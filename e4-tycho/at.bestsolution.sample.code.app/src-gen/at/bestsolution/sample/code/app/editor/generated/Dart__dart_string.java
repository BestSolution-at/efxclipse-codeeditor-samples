package at.bestsolution.sample.code.app.editor.generated;

public class Dart__dart_string extends org.eclipse.jface.text.rules.RuleBasedScanner {
	public Dart__dart_string() {
		org.eclipse.jface.text.rules.Token dart_stringToken = new org.eclipse.jface.text.rules.Token(new org.eclipse.jface.text.TextAttribute("dart.dart_string"));
		setDefaultReturnToken(dart_stringToken);
		org.eclipse.jface.text.rules.Token dart_string_interToken = new org.eclipse.jface.text.rules.Token(new org.eclipse.jface.text.TextAttribute("dart.dart_string_inter"));
		org.eclipse.jface.text.rules.IRule[] rules = new org.eclipse.jface.text.rules.IRule[1];
		rules[0] = new org.eclipse.jface.text.rules.SingleLineRule(
			  "${"
			, "}"
			, dart_string_interToken
			, (char)0
			, false);

		setRules(rules);
	}
}
