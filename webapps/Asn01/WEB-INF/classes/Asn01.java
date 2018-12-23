import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;
import javax.servlet.annotation.MultipartConfig;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@MultipartConfig(fileSizeThreshold = 6291456, // 6 MB
		maxFileSize = 10485760L, // 10 MB
		maxRequestSize = 20971520L // 20 MB
)
public class Asn01 extends HttpServlet {
    private static final long serialVersionUID = 5619951677845873534L;

    private static final String UPLOAD_DIR = "uploads";

		private static String getSubmittedFileName(Part part) {
				for (String cd : part.getHeader("content-disposition").split(";")) {
						if (cd.trim().startsWith("filename")) {
								String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
								return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
						}
				}
				return null;
		}

		@Override
		public void doGet(HttpServletRequest request, HttpServletResponse response)
											throws ServletException, IOException {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println("<html>" +
										   "<body>" +
										      "<form method=\"POST\">" +
										         "UserID: <input type=\"text\" name=\"UserID\"><br>" +
										         "Password: <input type=\"text\" name=\"Password\"><br>" +
										         "<input type=\"submit\" name=\"login\" value=\"Login\">" +
										      "</form>" +
										   "</body>" +
										"</html>");
		}

		@Override
		public void doPost(HttpServletRequest request, HttpServletResponse response)
										   throws ServletException, IOException {
				response.setContentType("text/html");
			  PrintWriter out = response.getWriter();

				if (request.getParameter("login") != null) {
						String userID = request.getParameter("UserID");
						String password = request.getParameter("Password");
						boolean accountExists = false;
						HttpSession session = request.getSession(true);

						if (session.isNew()) {
								session.setAttribute(userID, userID);
						}

						try {
								try {
								Class.forName("com.mysql.jdbc.Driver");
						} catch (Exception ex) {}
							Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/firstdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", "root", "itsasecret");
							Statement stmt = con.createStatement();
							ResultSet rs = stmt.executeQuery("SELECT * FROM Accounts");

							while (rs.next()) {
									if (userID.equalsIgnoreCase(rs.getString(1)) && password.equals(rs.getString(2))) {
											accountExists = true;
											break;
									}
							}

							stmt.close();
							con.close();
						} catch(SQLException ex) {
									System.out.println("\n--- SQLException caught ---\n");
									while (ex != null) {
											System.out.println("Message: " + ex.getMessage ());
											System.out.println("SQLState: " + ex.getSQLState ());
											System.out.println("ErrorCode: " + ex.getErrorCode ());
											ex = ex.getNextException();
											System.out.println("");
									}
							}

						if (accountExists) {
								String pictures = "<html>" +
																			"<body>" +
																					"<h2>" + "User ID: " + userID + "</h2><br>" +
																					"<img id=\"myImg\" src=\"image1.jpeg\" width=\"350px\" height=\"200px\"><br>" +
																					"<button type=\"button\" onclick=\"prevImg()\">Prev</button>" +
																					"<button type=\"button\" onclick=\"nextImg()\">Next</button>" +
																					"<button type=\"button\" onclick=\"startAuto()\">Auto</button>" +
																					"<button type=\"button\" onclick=\"stopAuto()\">Stop Auto</button><br><br>" +
																					"<form method=\"POST\">" +
																							"<p>Keyword:</p>" + "<input type=\"text\" name=\"Keyword\"/><br><br>" +
																							"<input type=\"submit\" name=\"Search\" value=\"Search\"/><br>" +
																					"</form>" +
																					"<script>" +
																							"var counter = 0;" +
																							"var images = [\"image1.jpeg\", \"image2.jpeg\", \"image3.jpeg\"];" +
																							"var timer;" +

																							"function prevImg() {" +
																									"var xhttp = new XMLHttpRequest();" +
																									"xhttp.onreadystatechange = function() {" +
																											"if (this.readyState == 4 && this.status == 200) {" +
																													"if (counter == 0) {" +
																															"document.getElementById(\"myImg\").src = images[2];" +
																															"counter = 2;" +
																													"} else if (counter == 1) {" +
																															"document.getElementById(\"myImg\").src = images[0];" +
																															"counter = 0;" +
																													"} else {" +
																															"document.getElementById(\"myImg\").src = images[1];" +
																															"counter = 1;" +
																													"}" +
																											"}" +
																									"};" +
																									"xhttp.open(\"GET\", \"/Asn01/Asn01_functions\", true);" +
																									"xhttp.send();" +
																							"}" +

																							"function nextImg() {" +
																									"var xhttp = new XMLHttpRequest();" +
																									"xhttp.onreadystatechange = function() {" +
																											"if (this.readyState == 4 && this.status == 200) {" +
																													"if (this.readyState == 4 && this.status == 200) {" +
																															"if (counter == 0) {" +
																																	"document.getElementById(\"myImg\").src = images[1];" +
																																	"counter = 1;" +
																															"} else if (counter == 1) {" +
																																	"document.getElementById(\"myImg\").src = images[2];" +
																																	"counter = 2;" +
																															"} else {" +
																																	"document.getElementById(\"myImg\").src = images[0];" +
																																	"counter = 0;" +
																															"}" +
																													"}" +
																											"}" +
																									"};" +
																									"xhttp.open(\"GET\", \"/Asn01/Asn01_functions\", true);" +
																									"xhttp.send();" +
																							"}" +

																							"function startAuto() {" +
																									"timer = setInterval(nextImg, 1000);" +
																							"}" +

																							"function stopAuto() {" +
																									"clearInterval(timer);" +
																							"}" +
																					"</script>" +
																					"<h1>Upload Picture</h1>" +
																					"<form method=\"POST\" enctype=\"multipart/form-data\">" +
																							"<input type=\"file\" name=\"fileName1\"/><br><br>" +
																							"Caption:<br><textarea name=\"Caption\" rows=\"4\" cols=\"50\"></textarea><br><br>" +
																							"<input type=\"submit\" name=\"upload\" value=\"Submit\"/>" +
																					"</form><br><br><br>" +
																					"<form method=\"POST\">" +
																							"<input type=\"submit\" name=\"logout\" value=\"Log Out\"/>" +
																				  "</form>" +
																			"</body>" +
																	"</html>";
								out.println(pictures);
						} else {
								String loginFailed = "<script>" + "alert(\"Login Failed\")" + "</script>";
								out.println(loginFailed + "<html>" +
																					   "<body>" +
																					      "<form method=\"POST\">" +
																					         "UserID: <input type=\"text\" name=\"UserID\"><br>" +
																					         "Password: <input type=\"text\" name=\"Password\"><br>" +
																					         "<input type=\"submit\" name=\"login\" value=\"Login\">" +
																					      "</form>" +
																					   "</body>" +
																					"</html>");
						}
				} else if (request.getParameter("upload") != null) {

						String caption = request.getParameter("Caption");

						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
						LocalDateTime now = LocalDateTime.now();

						response.setCharacterEncoding("UTF-8");
						// gets absolute path of the web application
						String applicationPath = request.getServletContext().getRealPath("");
						// constructs path of the directory to save uploaded file
						String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
						// creates upload folder if it does not exists
						File uploadFolder = new File(uploadFilePath);
						if (!uploadFolder.exists()) {
								uploadFolder.mkdirs();
						}
						PrintWriter writer = response.getWriter();
						// write all files in upload folder
						for (Part part : request.getParts()) {
								if (part != null && part.getSize() > 0) {
										//String fileName = part.getSubmittedFileName();
										String fileName = getSubmittedFileName(part);
										String contentType = part.getContentType();

										// allows only JPEG files to be uploaded
										if (!contentType.equalsIgnoreCase("image/jpeg")) {
												continue;
										}

										part.write(uploadFilePath + File.separator + fileName);

										try {
												try {
												Class.forName("com.mysql.jdbc.Driver");
										} catch (Exception ex) {}
											Connection con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/firstdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", "root", "itsasecret");
											Statement stmt2 = con2.createStatement();
											stmt2.executeUpdate("INSERT INTO Photos (Date, Caption, FileName) VALUES ('" +
																								dtf.format(now) + "', '" + caption + "', '" + getSubmittedFileName(part) + "')");
											stmt2.close();
											con2.close();
										} catch(SQLException ex) {
													System.out.println("\n--- SQLException caught ---\n");
													while (ex != null) {
															System.out.println("Message: " + ex.getMessage ());
															System.out.println("SQLState: " + ex.getSQLState ());
															System.out.println("ErrorCode: " + ex.getErrorCode ());
															ex = ex.getNextException();
															System.out.println("");
													}
											}

										writer.append("File successfully uploaded to "
												+ uploadFolder.getAbsolutePath()
												+ File.separator
												+ fileName
												+ "<br>\r\n");
								}
						}
				} else if (request.getParameter("logout") != null) {
						HttpSession session = request.getSession();
						session.removeAttribute("UserID");
						request.getSession().invalidate();
						response.sendRedirect("Asn01_functions");
				} else if (request.getParameter("Search") != null) {
						try {
								try {
								Class.forName("com.mysql.jdbc.Driver");
						} catch (Exception ex) {}
							String keywordCaption = request.getParameter("Keyword");
							Connection con3 = DriverManager.getConnection("jdbc:mysql://localhost:3306/firstdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", "root", "itsasecret");
							Statement stmt3 = con3.createStatement();
							ResultSet rs2 = stmt3.executeQuery("SELECT FileName FROM Photos WHERE Caption LIKE '%" + keywordCaption + "%';");
							String returnFileName = null;

							while (rs2.next()) {
									returnFileName = rs2.getString(1);
							}

							if (returnFileName != null && keywordCaption != "") {
									out.println("<html>" +
																	"<body>" +
																			"<img id=\"img\" src=\"\" width=\"350px\" height=\"200px\"><br>" +
																			"<script>" +
																					"var xhttp2 = new XMLHttpRequest();" +
																					"xhttp2.onreadystatechange = function() {" +
																							"if (this.readyState == 4 && this.status == 200) {" +
																									"document.getElementById(\"img\").src ='" + returnFileName + "';" +
																							"}" +
																					"};" +
																					"xhttp2.open(\"GET\", \"/Asn01/Asn01_functions\", true);" +
																					"xhttp2.send();" +
																			"</script>" +
																	"</body>" +
															"</html>");
						  } else {
									out.println("<html>"+
																	"<body>" +
																		"<p>Cannot find picture</p>" +
																	"</body>" +
															"</html>");
							}
							stmt3.close();
							con3.close();
						} catch(SQLException ex) {
									System.out.println("\n--- SQLException caught ---\n");
									while (ex != null) {
											System.out.println("Message: " + ex.getMessage ());
											System.out.println("SQLState: " + ex.getSQLState ());
											System.out.println("ErrorCode: " + ex.getErrorCode ());
											ex = ex.getNextException();
											System.out.println("");
									}
							}
					}
		}
}
