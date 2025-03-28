package yoshikihigo.tinypdg.graphviz;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.jdt.core.dom.CompilationUnit;

import yoshikihigo.tinypdg.ast.TinyPDGASTVisitor;
import yoshikihigo.tinypdg.cfg.CFG;
import yoshikihigo.tinypdg.cfg.edge.CFGEdge;
import yoshikihigo.tinypdg.cfg.node.CFGControlNode;
import yoshikihigo.tinypdg.cfg.node.CFGNode;
import yoshikihigo.tinypdg.cfg.node.CFGNodeFactory;
import yoshikihigo.tinypdg.pdg.PDG;
import yoshikihigo.tinypdg.pdg.edge.PDGControlDependenceEdge;
import yoshikihigo.tinypdg.pdg.edge.PDGDataDependenceEdge;
import yoshikihigo.tinypdg.pdg.edge.PDGEdge;
import yoshikihigo.tinypdg.pdg.edge.PDGExecutionDependenceEdge;
import yoshikihigo.tinypdg.pdg.node.PDGControlNode;
import yoshikihigo.tinypdg.pdg.node.PDGMethodEnterNode;
import yoshikihigo.tinypdg.pdg.node.PDGNode;
import yoshikihigo.tinypdg.pdg.node.PDGNodeFactory;
import yoshikihigo.tinypdg.pdg.node.PDGParameterNode;
import yoshikihigo.tinypdg.pe.MethodInfo;
import yoshikihigo.tinypdg.pe.ProgramElementInfo;

public class Writer {


	public static void main(String[] args) {
		
		Scanner scanner=new Scanner(System.in);
		
		System.out.println("Please enter the source directory of the java code:");
		String directory=scanner.nextLine();

		try {
			/*
			final Options options = new Options();

			{
				final Option d = new Option("d", "directory", true,
						"target directory");
				d.setArgName("directory");
				d.setArgs(1);
				d.setRequired(true);
				options.addOption(d);
			}E:\temp\java

			{
				final Option c = new Option("c", "ControlFlowGraph", true,
						"control flow graph");
				c.setArgName("file");
				c.setArgs(1);
				c.setRequired(false);
				options.addOption(c);
			}

			{
				final Option p = new Option("p", "ProgramDepencencyGraph",
						true, "program dependency graph");
				p.setArgName("file");
				p.setArgs(1);
				p.setRequired(false);
				options.addOption(p);
			}

			// {
			// final Option o = new Option("o", "optimize", true,
			// "remove unnecessary nodes from CFGs and PDGs");
			// o.setArgName("boolean");
			// o.setArgs(1);
			// o.setRequired(false);
			// options.addOption(o);
			// }

			// {
			// final Option a = new Option("a", "atomic", true,
			// "dissolve complicated statements into simple statements");
			// a.setArgName("boolean");
			// a.setArgs(1);
			// a.setRequired(false);
			// options.addOption(a);
			// }

			final CommandLineParser parser = new PosixParser();
			final CommandLine cmd = parser.parse(options, args);
			*/

			//final File target = new File(cmd.getOptionValue("d"));
			final File target = new File(directory);
			if (!target.exists()) {
				System.err
						.println("specified directory or file does not exist.");
				System.exit(0);
			}
			else {
				System.out.println("Exist!");
			}
			
			//获取目录下所有文件 wanght修改文件输出路径
			final List<File> files = getFiles(target);
			final List<MethodInfo> methods = new ArrayList<MethodInfo>();
			String fileName = new String();
			// wang add for file rebuild
			Path firstPath = target.toPath(); 
			String sname = "D:\\AllData\\DeepLineData\\Preprocess\\";
			Path directoryPath = target.toPath();
			String sparent = directoryPath.getFileName().toString();
			
			// 在sname下根据数据创建文件夹
			String sdot = sname + sparent + "\\dot\\";
			String sdfs = sname + sparent + "\\dfs\\";
			String errFile = sname + sparent + "\\err_files.txt";
			try {
				Files.createDirectories(Paths.get(sdot));
				Files.createDirectories(Paths.get(sdfs));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			for (final File file : files) {
				methods.clear();
				final CompilationUnit unit = TinyPDGASTVisitor.createAST(file);
				final List<MethodInfo> m = new ArrayList<MethodInfo>();
				final TinyPDGASTVisitor visitor = new TinyPDGASTVisitor(
						file.getAbsolutePath(), unit, methods);
				unit.accept(visitor);
				//methods.clear();
				methods.addAll(m);
			//}
			
			//while(true) {
				//System.out.println("Please enter the graph you want to generate(CFG or PDG):");
				//String graph=scanner.nextLine();
			
				//if (graph.equals("CFG")||graph.equals("cfg")) {
//					System.out.println("building and outputing CFGs ...");
//					String filename = file.getName();
//					String[] all = file.getAbsolutePath().split("\\\\");
//					System.out.println(file.getAbsolutePath());
//					String tmpName = "";
//					if(all!=null && all.length > 0) {
//						for(int j = 4; j<all.length-1; j++) {
//							System.out.print(all[j] + " ");
//							tmpName += all[j];
//							tmpName += "_";
//							
//						}
//						System.out.println();
//						tmpName += all[all.length-1];
//					}else {
//						tmpName = filename;
//					}
//					
//					System.out.println(tmpName);
//					final BufferedWriter writer = new BufferedWriter(
//							new FileWriter(directory+"\\"+tmpName + ".dot"));
//
//					writer.write("digraph CFG {");
//					writer.newLine();
//
//					final CFGNodeFactory nodeFactory = new CFGNodeFactory();
//
//					int createdGraphNumber = 0;
//					Map<String, Integer> me = new HashMap<String, Integer>();
//					
//					for (final MethodInfo method : methods) {
//						final CFG cfg = new CFG(method, nodeFactory);
//						cfg.build();
//						cfg.removeSwitchCases();
//						cfg.removeJumpStatements();
//						String methodName = getMethodSignature((MethodInfo) cfg.core).split(" ")[0];
//						if(me.containsKey(methodName)) {
//							int tmp = me.get(methodName);
//							tmp = tmp + 1;
//							me.put(methodName, tmp);
//							String stmp = methodName + tmp;
//							writeMethodCFG(cfg, createdGraphNumber++, writer, filename, tmpName+"_"+stmp);
//						}else {
//							me.put(methodName, 0);
//							writeMethodCFG(cfg, createdGraphNumber++, writer, filename, tmpName+"_"+methodName+0);
//						}
//						
//					}
//
//					writer.write("}");
//
//					writer.close();

			//	}
			//	if (graph.equals("PDG")||graph.equals("pdg")) {

			fileName =file.getName();
			Path filePath = file.toPath();
			Path relativePath = directoryPath.relativize(filePath);
			Path parentPath = relativePath.getParent();
			// 此时tmpName 已经是绝对路径，没有保存原始文件结构，使用下滑线将路径保存在了文件名中
			String tmpName = sdot + relativePath.toString().replaceAll("\\\\","_");
			String dfsName = sdfs + relativePath.toString().replaceAll("\\\\","_");
			
			// 也可以保存原始文件结构
			//String tmpName = sdot + relativePath.toString();
			
			System.out.println(tmpName);
			// System.out.println("building and outputing PDGs"+fileName+" ...");
			try {
				final BufferedWriter writer = new BufferedWriter(
						new FileWriter(tmpName + ".dot"));

				writer.write("digraph {");
				writer.newLine();
				Map<String, Integer> me = new HashMap<String, Integer>();
				int createdGraphNumber = 0;
				for (final MethodInfo method : methods) {
				
					final PDG pdg = new PDG(method, new PDGNodeFactory(),
							new CFGNodeFactory(), true, true, true);
					pdg.build();
					final MethodInfo method2 = pdg.unit;
					String methodName = getMethodSignature(method2).split(" ")[0];
					
					System.out.println(methodName);
					if(me.containsKey(methodName)) {
						int tmp = me.get(methodName);
						tmp = tmp + 1;
						me.put(methodName, tmp);
						String stmp = methodName + tmp;
						writePDG(pdg, createdGraphNumber++, writer,  dfsName+"_"+stmp);
					}else {
						me.put(methodName, 0);
						writePDG(pdg, createdGraphNumber++, writer,  dfsName+"_"+methodName+0);
					}
				}
				writer.write("}");
				writer.close();
			}
			catch (Exception e) {
	            System.err.println("捕捉到异常: " + e.getMessage());
	            try (FileWriter writer = new FileWriter(errFile, true)) {
	                writer.write(relativePath.toString().replaceAll("\\\\","_")+"\n");
	                System.out.println("文件追加写入成功！");
	            } catch (IOException e1) {
	                System.err.println("追加写入文件时发生错误: " + e1.getMessage());
	            }
	        }
			System.out.println("successfully finished.");
			}
			//}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			// System.exit(0);
		}
	}

	static private void writeMethodCFG(final CFG cfg,
			final int createdGraphNumber, final BufferedWriter writer, String name, String methodName)
			throws IOException {

		writer.write("subgraph cluster");
		writer.write(Integer.toString(createdGraphNumber));
		writer.write(" {");
		writer.newLine();

		writer.write("label = \"");
		writer.write(getMethodSignature((MethodInfo) cfg.core));   /////////////////ciaran
		writer.write("\";");
		writer.newLine();

		final SortedMap<CFGNode<? extends ProgramElementInfo>, Integer> nodeLabels = new TreeMap<CFGNode<? extends ProgramElementInfo>, Integer>();
		for (final CFGNode<?> node : cfg.getAllNodes()) {
			nodeLabels.put(node, nodeLabels.size());
		}
		
		ArrayList<String> arr = new ArrayList<String>();
		for (final Map.Entry<CFGNode<? extends ProgramElementInfo>, Integer> entry : nodeLabels
				.entrySet()) {

			final CFGNode<? extends ProgramElementInfo> node = entry.getKey();
			final Integer label = entry.getValue();

			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			writer.write(Integer.toString(label));
			writer.write(" [style = filled, label = \"");
			writer.write(node.getText().replace("\"", "\\\"")
					.replace("\\\\\"", "\\\\\\\""));
			writer.write("\"");
			arr.add(node.getText().replace("\"", "\\\"").replace("\\\\\"", "\\\\\\\"").split("\n")[0]);
			final CFGNode<? extends ProgramElementInfo> enterNode = cfg
					.getEnterNode();
			final SortedSet<CFGNode<? extends ProgramElementInfo>> exitNodes = cfg
					.getExitNodes();

			if (enterNode == node) {
				writer.write(", fillcolor = aquamarine");
			} else if (exitNodes.contains(node)) {
				writer.write(", fillcolor = deeppink");
			} else {
				writer.write(", fillcolor = white");
			}

			if (node instanceof CFGControlNode) {
				writer.write(", shape = diamond");
			} else {
				writer.write(", shape = ellipse");
			}

			writer.write("];");
			writer.newLine();
		}

		writeCFGEdges(cfg, nodeLabels, createdGraphNumber, writer);
		
		if(arr.size() > 0) {		
			
			String sname = "C:\\Users\\xk\\Desktop\\test\\test\\";
			
			File fout = new File(sname + methodName+".txt");
			
			FileOutputStream fos = new FileOutputStream(fout); 
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos)); 
			
			
			for(int i = 0; i<arr.size(); i++) {
				bw.write(arr.get(i)+"\n");
		}
		
		bw.close();}

		writer.write("}");
		writer.newLine();
	}

	static private void writeCFGEdges(
			final CFG cfg,
			final Map<CFGNode<? extends ProgramElementInfo>, Integer> nodeLabels,
			final int createdGraphNumber, final BufferedWriter writer)
			throws IOException {

		if (null == cfg) {
			return;
		}

		final SortedSet<CFGEdge> edges = new TreeSet<CFGEdge>();
		for (final CFGNode<?> node : cfg.getAllNodes()) {
			edges.addAll(node.getBackwardEdges());
			edges.addAll(node.getForwardEdges());
		}

		for (final CFGEdge edge : edges) {
			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			writer.write(Integer.toString(nodeLabels.get(edge.fromNode)));
			writer.write(" -> ");
			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			writer.write(Integer.toString(nodeLabels.get(edge.toNode)));
			writer.write(" [style = solid, label=\""
					+ edge.getDependenceString() + "\"];");
			writer.newLine();
		}
	}

	static private void writePDG(final PDG pdg, final int createdGraphNumber,
			final BufferedWriter writer, String method2) throws IOException {

		final MethodInfo method = pdg.unit;

		writer.write("subgraph cluster");
		writer.write(Integer.toString(createdGraphNumber));
		writer.write(" {");
		writer.newLine();

		writer.write("label = \"");
		writer.write(getMethodSignature(method));  //////////////////////ciaran 
		writer.write("\";");
		writer.newLine();
		
		final Map<PDGNode<?>, Integer> nodeLabels = new HashMap<PDGNode<?>, Integer>();
		for (final PDGNode<?> node : pdg.getAllNodes()) {
			nodeLabels.put(node, nodeLabels.size());
		}
		ArrayList<String> arr = new ArrayList<String>();
		for(int i = 0; i<nodeLabels.size(); i++) arr.add("");
		for (final Map.Entry<PDGNode<?>, Integer> entry : nodeLabels.entrySet()) {
			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			writer.write(Integer.toString(entry.getValue()));
			////////////////////////////////////////////////////////////////////////////////////////////////////
			writer.write(" [style = filled, label = \"");
			writer.write(entry.getKey().getText().replace("\"", "\\\"")
					.replace("\\\\\"", "\\\\\\\""));
			writer.write("\"");
//			System.out.println("++++++++++++" + entry.getKey().getText().replace("\"", "\\\"")
//					.replace("\\\\\"", "\\\\\\\""));
			String tt = entry.getKey().getText().replace("\"", "\\\"").replace("\\\\\"", "\\\\\\\"").split("\n")[0];
			
			System.out.println("tt=====" + tt);
			arr.set(entry.getValue(), tt);
			//arr.add(tt);
			/////////////////////////////////////////////////////////////////////////////////////////////////////
			final PDGNode<?> node = entry.getKey();
			if (node instanceof PDGMethodEnterNode) {
				writer.write(", fillcolor = aquamarine");
			} else if (pdg.getExitNodes().contains(node)) {
				writer.write(", fillcolor = deeppink");
			} else if (node instanceof PDGParameterNode) {
				writer.write(", fillcolor = tomato");
			} else {
				writer.write(", fillcolor = white");
			}

			if (node instanceof PDGControlNode) {
				writer.write(", shape = diamond");
			} else if (node instanceof PDGParameterNode) {
				writer.write(", shape = box");
			} else {
				writer.write(", shape = ellipse");
			}
			
			writer.write("];");
			writer.newLine();
			
		}
		
		if(arr.size() > 0) {		
			
			// String sname = "D:\\AllData\\DeepLineData\\Preprocess\\dfs\\";
			
			File fout = new File(method2+".txt");
			
			FileOutputStream fos = new FileOutputStream(fout); 
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos)); 
			
			
			for(int i = 0; i<arr.size(); i++) {
				bw.write(arr.get(i)+"\n");
		}
		
		bw.close();}

		for (final PDGEdge edge : pdg.getAllEdges()) {
			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			writer.write(Integer.toString(nodeLabels.get(edge.fromNode)));
			writer.write(" -> ");
			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			writer.write(Integer.toString(nodeLabels.get(edge.toNode)));
			if (edge instanceof PDGDataDependenceEdge) {
				writer.write(" [style = solid, label=\""
						+ edge.getDependenceString() + "\"]");
			} else if (edge instanceof PDGControlDependenceEdge) {
				writer.write(" [style = dotted, label=\""
						+ edge.getDependenceString() + "\"]");
			} else if (edge instanceof PDGExecutionDependenceEdge) {
				writer.write(" [style = bold, label=\""
						+ edge.getDependenceString() + "\"]");
			}
			writer.write(";");
			writer.newLine();
		}

		writer.write("}");
		writer.newLine();
	}

	static private List<File> getFiles(final File file) {

		final List<File> files = new ArrayList<File>();

		if (file.isFile() && file.getName().endsWith(".java")) {
			files.add(file); 
		}

		else if (file.isDirectory()) {
			for (final File child : file.listFiles()) {
				final List<File> children = getFiles(child);
				files.addAll(children);
			}
		}

		return files;
	}

	static private String getMethodSignature(final MethodInfo method) {

		final StringBuilder text = new StringBuilder();

		text.append(method.name);
		text.append(" <");
		text.append(method.startLine);
		text.append("...");
		text.append(method.endLine);
		text.append(">");

		return text.toString();
	}
}
