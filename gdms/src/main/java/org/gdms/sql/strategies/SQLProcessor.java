package org.gdms.sql.strategies;

import java.io.ByteArrayInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gdms.data.DataSourceFactory;
import org.gdms.data.ExecutionException;
import org.gdms.driver.DriverException;
import org.gdms.driver.ObjectDriver;
import org.gdms.sql.parser.Node;
import org.gdms.sql.parser.ParseException;
import org.gdms.sql.parser.SQLEngine;
import org.gdms.sql.parser.SimpleNode;
import org.gdms.sql.parser.Token;
import org.orbisgis.IProgressMonitor;

public class SQLProcessor {

	private DataSourceFactory dsf;

	public SQLProcessor(DataSourceFactory dsf) {
		this.dsf = dsf;
	}

	/**
	 * Executes the specified instruction using the specified DataSourceFactory
	 *
	 * @param sql
	 * @param pm
	 * @return
	 * @throws ParseException
	 *             If the sql is not well formed
	 * @throws SemanticException
	 *             If the instruction contains semantic errors: unknown or
	 *             ambiguous field references, operations with incompatible
	 *             types, etc.
	 * @throws DriverException
	 *             If there is a problem accessing the sources
	 * @throws ExecutionException
	 *             If there is a problem while executing the SQL
	 */
	public ObjectDriver execute(String sql, IProgressMonitor pm)
			throws ParseException, SemanticException, DriverException,
			ExecutionException {

		Instruction instr = prepareInstruction(sql);

		// Execution
		return instr.execute(pm);
	}

	private Operator parse(String sql) throws ParseException,
			SemanticException, DriverException {
		if (!sql.trim().endsWith(";")) {
			sql += ";";
		}

		SQLEngine parser = new SQLEngine(new ByteArrayInputStream(sql
				.getBytes()));
		try {
			parser.SQLStatement();
		} catch (ParseException e) {
			throw new ParseException("Parse error in " + sql + ": "
					+ e.getMessage());
		}
		LogicTreeBuilder lp = new LogicTreeBuilder(dsf);
		Operator op = (Operator) lp
				.buildTree((SimpleNode) parser.getRootNode());
		return op;
	}

	/**
	 * Prepares the instruction for the execution. The returned value is
	 * optimized and ready to execute
	 *
	 * @param script
	 *            text containing the sql script
	 * @return
	 * @throws SemanticException
	 *             If the instruction contains semantic errors: unknown or
	 *             ambiguous field references, operations with incompatible
	 *             types, etc.
	 * @throws DriverException
	 *             If there is an error accessing the sources involved in any of
	 *             the queries
	 * @throws ParseException
	 *             If there is any instruction with parse errors in the script
	 */
	public Instruction prepareInstruction(String sql) throws ParseException,
			SemanticException, DriverException {
		// Compilation
		Operator op = parse(sql);

		// Preprocessor
		Preprocessor p = new Preprocessor(op);
		p.validate();

		return new Instruction(dsf, op, sql);
	}

	/**
	 * Gets the instructions found in the script
	 *
	 * @param script
	 *            text containing the sql script
	 * @return
	 * @throws SemanticException
	 *             If the instruction contains semantic errors: unknown or
	 *             ambiguous field references, operations with incompatible
	 *             types, etc.
	 * @throws DriverException
	 *             If there is an error accessing the sources involved in any of
	 *             the queries
	 * @throws ParseException
	 *             If there is any instruction with parse errors in the script
	 */
	public Instruction[] prepareScript(String script) throws SemanticException,
			DriverException, ParseException {
		if (!script.trim().endsWith(";")) {
			script += ";";
		}

		SQLEngine parser = new SQLEngine(new ByteArrayInputStream(script
				.getBytes()));
		parser.SQLScriptBody();
		LogicTreeBuilder lp = new LogicTreeBuilder(dsf);
		return lp.getInstructions((SimpleNode) parser.getRootNode());
	}

	public String getScriptComment(String script) throws ParseException {
		SQLEngine parser = new SQLEngine(new ByteArrayInputStream(script
				.getBytes()));
		parser.SQLScript();
		Node root = parser.getRootNode();
		if (root.jjtGetNumChildren() == 2) {
			int index;
			index = getPosition(script, root);
			String comment = script.substring(0, index - 2);
			return comment.substring(2);
		} else {
			return null;
		}
	}

	private int getPosition(String script, Node root) {
		Token firstToken = ((SimpleNode) root.jjtGetChild(1)).first_token;
		int line = firstToken.beginLine - 1; // 0-based line
		int linePos = 0;
		while (line > 0) {
			String patternString = "\n";
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(script);
			if (!matcher.find()) {
				throw new RuntimeException("bug!");
			}
			int crPosition = matcher.start() + 1;
			linePos += crPosition;

			script = script.substring(crPosition);
			line--;
		}
		return linePos + firstToken.beginColumn - 1;
	}

	public String getScriptBody(String script) throws ParseException {
		SQLEngine parser = new SQLEngine(new ByteArrayInputStream(script
				.getBytes()));
		parser.SQLScript();
		Node root = parser.getRootNode();

		int index;
		if (root.jjtGetNumChildren() == 2) {
			index = getPosition(script, root);
		} else {
			index = 0;
		}

		return script.substring(index);
	}
}
