package pjpl.s7.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Klasa bazowa dla klas wykonujących zrzutów stanu PLC.
 * Wszystkie klasy pochodne zapisuję oddzielnie każdy blok danych, w osobnym pliku, tabeli bazy danych ...
 */
abstract public class DumpExpert{
	public abstract void dump(MemoryByteClip data);
}
