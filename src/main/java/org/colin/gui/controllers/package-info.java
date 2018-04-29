/**
 * <h1>Controllers responsible for business logic behind views.</h1>
 * <ul>
 *
 * <li><b>{@link org.colin.gui.controllers.MainController}</b>
 * - main controller responsible for handling the logic behind the main view.
 * This class implements {@link org.colin.actions.FileReceiver} for the purposes of handling file-loading
 * callback initiated by the view. The class also implements {@link java.awt.dnd.DropTargetListener} to allow users to open files via drag-and-drop.
 * <br>
 * <img alt="main view" src="doc-files/main.png">
 * </li>
 *
 * <li><b>{@link org.colin.gui.controllers.AuditController}</b> - controller responsible for handling logic related to the
 * auditing view. Primarily used for handling events related to the navigation, graphing ({@link org.colin.gui.graph.GraphView}), and general auditing of opened files.
 * <br>
 * <img style="max-width: 85%;" alt="lang selection view" src="doc-files/audit.png">
 * </li>
 *
 * <li><b>{@link org.colin.gui.controllers.AuditorController}</b> - audit controller handles the logic behind the selection/reduction of audit context ranges and the inputting of the audit comment.
 * <br>
 * <img alt="lang selection view" src="doc-files/auditor.png"></li>
 *
 * <li><b>{@link org.colin.gui.controllers.ParseProblemController}</b> - controller solely used for the initialisation of the {@link org.colin.gui.views.ParseProblemView} to display parser problems/errors.
 * <br>
 * <img alt="lang selection view" src="doc-files/problem.png"></li>
 *
 * <li><b>{@link org.colin.gui.controllers.LangSelectionController}</b> - controller used for managing the selection/switching of localisation.
 * <br>
 * <img alt="lang selection view" src="doc-files/lang.png"></li>
 * </ul>
 *
 * @author Colin J. Barr
 * @version 0.0.1
 */
package org.colin.gui.controllers;