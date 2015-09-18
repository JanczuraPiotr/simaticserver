package pjpl.s7.util;

/**
 * Klasa bazowa dla klas wykonujących zrzutów stanu PLC.
 * Wszystkie klasy pochodne zapisuję oddzielnie każdy blok danych, w osobnym pliku, tabeli bazy danych ...
 *
 * @autor Piotr Janczura <piotr@janczura.pl>
 */
abstract public class DumpExpert{
	public abstract void dump(MemByteClip data);
}
