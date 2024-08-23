package io.micronaut.starter.cli.command;

import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.Buffer;
import org.jline.reader.EndOfFileException;
import org.jline.reader.Expander;
import org.jline.reader.Highlighter;
import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.reader.MaskingCallback;
import org.jline.reader.ParsedLine;
import org.jline.reader.Parser;
import org.jline.reader.UserInterruptException;
import org.jline.reader.Widget;
import org.jline.terminal.MouseEvent;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;

import java.io.File;
import java.util.Collection;
import java.util.Map;

class StubbedLineReader implements LineReader {

    private final CommandSupplier commandSupplier;

    StubbedLineReader(CommandSupplier commandSupplier) {
        this.commandSupplier = commandSupplier;
    }

    @Override
    public Map<String, KeyMap<Binding>> defaultKeyMaps() {
        return null;
    }

    @Override
    public String readLine() throws UserInterruptException, EndOfFileException {
        return null;
    }

    @Override
    public String readLine(Character character) throws UserInterruptException, EndOfFileException {
        return null;
    }

    @Override
    public String readLine(String s) throws UserInterruptException, EndOfFileException {
        return commandSupplier.nextCommand();
    }

    @Override
    public String readLine(String s, Character character) throws UserInterruptException, EndOfFileException {
        return null;
    }

    @Override
    public String readLine(String s, Character character, String s1) throws UserInterruptException, EndOfFileException {
        return null;
    }

    @Override
    public String readLine(String s, String s1, Character character, String s2) throws UserInterruptException, EndOfFileException {
        return null;
    }

    @Override
    public String readLine(String s, String s1, MaskingCallback maskingCallback, String s2) throws UserInterruptException, EndOfFileException {
        return null;
    }

    @Override
    public void printAbove(String s) {

    }

    @Override
    public void printAbove(AttributedString attributedString) {

    }

    @Override
    public boolean isReading() {
        return false;
    }

    @Override
    public LineReader variable(String s, Object o) {
        return null;
    }

    @Override
    public LineReader option(Option option, boolean b) {
        return null;
    }

    @Override
    public void callWidget(String s) {

    }

    @Override
    public Map<String, Object> getVariables() {
        return null;
    }

    @Override
    public Object getVariable(String s) {
        return null;
    }

    @Override
    public void setVariable(String s, Object o) {

    }

    @Override
    public boolean isSet(Option option) {
        return false;
    }

    @Override
    public void setOpt(Option option) {

    }

    @Override
    public void unsetOpt(Option option) {

    }

    @Override
    public Terminal getTerminal() {
        return null;
    }

    @Override
    public Map<String, Widget> getWidgets() {
        return null;
    }

    @Override
    public Map<String, Widget> getBuiltinWidgets() {
        return null;
    }

    @Override
    public Buffer getBuffer() {
        return null;
    }

    @Override
    public String getAppName() {
        return null;
    }

    @Override
    public void runMacro(String s) {

    }

    @Override
    public MouseEvent readMouseEvent() {
        return null;
    }

    @Override
    public History getHistory() {
        return null;
    }

    @Override
    public Parser getParser() {
        return null;
    }

    @Override
    public Highlighter getHighlighter() {
        return null;
    }

    @Override
    public Expander getExpander() {
        return null;
    }

    @Override
    public Map<String, KeyMap<Binding>> getKeyMaps() {
        return null;
    }

    @Override
    public String getKeyMap() {
        return null;
    }

    @Override
    public boolean setKeyMap(String s) {
        return false;
    }

    @Override
    public KeyMap<Binding> getKeys() {
        return null;
    }

    @Override
    public ParsedLine getParsedLine() {
        return null;
    }

    @Override
    public String getSearchTerm() {
        return null;
    }

    @Override
    public RegionType getRegionActive() {
        return null;
    }

    @Override
    public int getRegionMark() {
        return 0;
    }

    @Override
    public void addCommandsInBuffer(Collection<String> collection) {

    }

    @Override
    public void editAndAddInBuffer(File file) throws Exception {

    }

    @Override
    public String getLastBinding() {
        return null;
    }

    @Override
    public String getTailTip() {
        return null;
    }

    @Override
    public void setTailTip(String s) {

    }

    @Override
    public void setAutosuggestion(SuggestionType suggestionType) {

    }

    @Override
    public SuggestionType getAutosuggestion() {
        return null;
    }

    @Override
    public void zeroOut() {

    }
}
