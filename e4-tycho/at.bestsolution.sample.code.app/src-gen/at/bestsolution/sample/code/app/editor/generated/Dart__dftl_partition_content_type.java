package at.bestsolution.sample.code.app.editor.generated;

public class Dart__dftl_partition_content_type extends org.eclipse.jface.text.rules.RuleBasedScanner {
	public Dart__dftl_partition_content_type() {
		org.eclipse.jface.text.rules.Token dart_defaultToken = new org.eclipse.jface.text.rules.Token(new org.eclipse.jface.text.TextAttribute("dart.dart_default"));
		setDefaultReturnToken(dart_defaultToken);
		org.eclipse.jface.text.rules.Token dart_operatorToken = new org.eclipse.jface.text.rules.Token(new org.eclipse.jface.text.TextAttribute("dart.dart_operator"));
		org.eclipse.jface.text.rules.Token dart_bracketToken = new org.eclipse.jface.text.rules.Token(new org.eclipse.jface.text.TextAttribute("dart.dart_bracket"));
		org.eclipse.jface.text.rules.Token dart_keywordToken = new org.eclipse.jface.text.rules.Token(new org.eclipse.jface.text.TextAttribute("dart.dart_keyword"));
		org.eclipse.jface.text.rules.Token dart_keyword_1Token = new org.eclipse.jface.text.rules.Token(new org.eclipse.jface.text.TextAttribute("dart.dart_keyword_1"));
		org.eclipse.jface.text.rules.Token dart_keyword_2Token = new org.eclipse.jface.text.rules.Token(new org.eclipse.jface.text.TextAttribute("dart.dart_keyword_2"));
		org.eclipse.jface.text.rules.Token dart_builtin_typesToken = new org.eclipse.jface.text.rules.Token(new org.eclipse.jface.text.TextAttribute("dart.dart_builtin_types"));
		org.eclipse.jface.text.rules.IRule[] rules = new org.eclipse.jface.text.rules.IRule[4];
		rules[0] = new org.eclipse.jface.text.source.CharacterRule(dart_operatorToken, new char[] {';','.','=','/','\\','+','-','*','<','>',':','?','!',',','|','&','^','%','~'});
		rules[1] = new org.eclipse.jface.text.source.CharacterRule(dart_bracketToken, new char[] {'(',')','{','}','[',']'});
		rules[2] = new org.eclipse.jface.text.rules.WhitespaceRule(Character::isWhitespace);

		org.eclipse.jface.text.source.JavaLikeWordDetector wordDetector= new org.eclipse.jface.text.source.JavaLikeWordDetector();
		org.eclipse.jface.text.rules.CombinedWordRule combinedWordRule= new org.eclipse.jface.text.rules.CombinedWordRule(wordDetector, dart_defaultToken);
		{
			org.eclipse.jface.text.rules.CombinedWordRule.WordMatcher dart_keywordWordRule = new org.eclipse.jface.text.rules.CombinedWordRule.WordMatcher();
			dart_keywordWordRule.addWord("break", dart_keywordToken);
			dart_keywordWordRule.addWord("case", dart_keywordToken);
			dart_keywordWordRule.addWord("catch", dart_keywordToken);
			dart_keywordWordRule.addWord("class", dart_keywordToken);
			dart_keywordWordRule.addWord("const", dart_keywordToken);
			dart_keywordWordRule.addWord("continue", dart_keywordToken);
			dart_keywordWordRule.addWord("default", dart_keywordToken);
			dart_keywordWordRule.addWord("do", dart_keywordToken);
			dart_keywordWordRule.addWord("else", dart_keywordToken);
			dart_keywordWordRule.addWord("enum", dart_keywordToken);
			dart_keywordWordRule.addWord("extends", dart_keywordToken);
			dart_keywordWordRule.addWord("false", dart_keywordToken);
			dart_keywordWordRule.addWord("final", dart_keywordToken);
			dart_keywordWordRule.addWord("finally", dart_keywordToken);
			dart_keywordWordRule.addWord("for", dart_keywordToken);
			dart_keywordWordRule.addWord("if", dart_keywordToken);
			dart_keywordWordRule.addWord("in", dart_keywordToken);
			dart_keywordWordRule.addWord("is", dart_keywordToken);
			dart_keywordWordRule.addWord("new", dart_keywordToken);
			dart_keywordWordRule.addWord("null", dart_keywordToken);
			dart_keywordWordRule.addWord("rethrow", dart_keywordToken);
			dart_keywordWordRule.addWord("return", dart_keywordToken);
			dart_keywordWordRule.addWord("super", dart_keywordToken);
			dart_keywordWordRule.addWord("switch", dart_keywordToken);
			dart_keywordWordRule.addWord("this", dart_keywordToken);
			dart_keywordWordRule.addWord("throw", dart_keywordToken);
			dart_keywordWordRule.addWord("true", dart_keywordToken);
			dart_keywordWordRule.addWord("try", dart_keywordToken);
			dart_keywordWordRule.addWord("var", dart_keywordToken);
			dart_keywordWordRule.addWord("void", dart_keywordToken);
			dart_keywordWordRule.addWord("while", dart_keywordToken);
			dart_keywordWordRule.addWord("with", dart_keywordToken);
			combinedWordRule.addWordMatcher(dart_keywordWordRule);
		}
		{
			org.eclipse.jface.text.rules.CombinedWordRule.WordMatcher dart_keyword_1WordRule = new org.eclipse.jface.text.rules.CombinedWordRule.WordMatcher();
			dart_keyword_1WordRule.addWord("abstract", dart_keyword_1Token);
			dart_keyword_1WordRule.addWord("as", dart_keyword_1Token);
			dart_keyword_1WordRule.addWord("assert", dart_keyword_1Token);
			dart_keyword_1WordRule.addWord("deferred", dart_keyword_1Token);
			dart_keyword_1WordRule.addWord("dynamic", dart_keyword_1Token);
			dart_keyword_1WordRule.addWord("export", dart_keyword_1Token);
			dart_keyword_1WordRule.addWord("external", dart_keyword_1Token);
			dart_keyword_1WordRule.addWord("factory", dart_keyword_1Token);
			dart_keyword_1WordRule.addWord("get", dart_keyword_1Token);
			dart_keyword_1WordRule.addWord("implements", dart_keyword_1Token);
			dart_keyword_1WordRule.addWord("import", dart_keyword_1Token);
			dart_keyword_1WordRule.addWord("library", dart_keyword_1Token);
			dart_keyword_1WordRule.addWord("operator", dart_keyword_1Token);
			dart_keyword_1WordRule.addWord("part", dart_keyword_1Token);
			dart_keyword_1WordRule.addWord("set", dart_keyword_1Token);
			dart_keyword_1WordRule.addWord("static", dart_keyword_1Token);
			dart_keyword_1WordRule.addWord("typedef", dart_keyword_1Token);
			combinedWordRule.addWordMatcher(dart_keyword_1WordRule);
		}
		{
			org.eclipse.jface.text.rules.CombinedWordRule.WordMatcher dart_keyword_2WordRule = new org.eclipse.jface.text.rules.CombinedWordRule.WordMatcher();
			dart_keyword_2WordRule.addWord("async", dart_keyword_2Token);
			dart_keyword_2WordRule.addWord("async*", dart_keyword_2Token);
			dart_keyword_2WordRule.addWord("await", dart_keyword_2Token);
			dart_keyword_2WordRule.addWord("sync*", dart_keyword_2Token);
			dart_keyword_2WordRule.addWord("yield", dart_keyword_2Token);
			dart_keyword_2WordRule.addWord("yield*", dart_keyword_2Token);
			combinedWordRule.addWordMatcher(dart_keyword_2WordRule);
		}
		{
			org.eclipse.jface.text.rules.CombinedWordRule.WordMatcher dart_builtin_typesWordRule = new org.eclipse.jface.text.rules.CombinedWordRule.WordMatcher();
			dart_builtin_typesWordRule.addWord("num", dart_builtin_typesToken);
			dart_builtin_typesWordRule.addWord("String", dart_builtin_typesToken);
			dart_builtin_typesWordRule.addWord("bool", dart_builtin_typesToken);
			dart_builtin_typesWordRule.addWord("int", dart_builtin_typesToken);
			dart_builtin_typesWordRule.addWord("double", dart_builtin_typesToken);
			dart_builtin_typesWordRule.addWord("List", dart_builtin_typesToken);
			dart_builtin_typesWordRule.addWord("Map", dart_builtin_typesToken);
			combinedWordRule.addWordMatcher(dart_builtin_typesWordRule);
		}
		rules[3] = combinedWordRule;
		setRules(rules);
	}
}
