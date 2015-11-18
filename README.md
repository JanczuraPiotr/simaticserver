SimaticServer
============

Projekty ** Simatic* ** tworzą oprogramowanie do komunikacji ze sterownikami przemysłowymi Simatic S7-1200 firmy  Simens.
Prawdopodobnie będą mogły pracować z pozostałymi sterownikami firmy Simens obsługującymi protokół PROFINET ale miałem dostęp tylko do sterownika S7-1200.
Nie są kompletnym rozwiązaniem a jedynie prototypem utworzonym w celu poznania możliwości zastosowania technologii webowej do nadzoru nad sieciami przemysłowymi.

Ten moduł wykorzystuje bibliotekę Moka7 : https://github.com/Gahzayah/Moka7test za pośrednictwem której komunikuje się ze sterownikiem S7 poprzez połączenie TCP/IP w celu pobierania i zmieniania zmiennych procesora.

W skrócie praca modułu SimaticServer polega na cyklicznym odczycie zmiennych ze sterownika. Zmienne pobierane są w postaci zrzutu zmiennych do tablicy bajtowej wykonanego przez sterownik i jako tablica bajtów odczytywany jest ze strumienia wejściowego przez SimaticServer. Modyfikacja zmiennej sterownika również wymaga zrzucenia zmiennej do tablicy bajtowej i jako tablica wysyłana jest pod określony adres w tablicy zmiennych sterownika.

Odczyt ze sterownika realizuje metoda:
 S7Client::ReadArea(int Area, int DBNumber, int Start, int Amount, byte[] Data)
 
Zapis do sterownika realizuje metoda:
 S7Client::WriteArea(int Area, int DBNumber, int Start, int Amount, byte[] Data)
 
Podstawowe funkcje SimaticServer według pomysłodawcy to ciągły zapis stanu zmiennych sterowanego procesu. Odbywa się poprzez zapis do katalogu lokalnego, opróżnianego z najstarszych zapisów i do bazy danych MySql. Zapis odbywa się niezależnie. Częstotliwość zapisu ustalono na 1s, wartość ta ustalana jest w pliku konfiguracyjnym. Przy 4 zmiennych i po jednym porcie wyjściowym i wejściowym cykl trwa około 20ms i wydaje się że częstotliwość cyklu można znacznie zwiększyć ale należy mieć na uwadze, że w rzeczywistych warunkach gdzie pracuje kilka sterowników obsługujących po kilkadziesiąt zmiennych procesowych cykl znacznie się wydłuży.

Przegląd zebranych danych odbywa się za pośrednictwem pakietu SimaticWeb utworzonego w Symfony2, który przetwarza dane z bazy danych wcześniej spisane tam przez SimaticServer.

Dodatkowo SimaticServer umożliwia nadzór nad procesem w czasie rzeczywistym. Wymaga to podłączenia się do SimaticServer za pomocą protokołu TCP/IP. Przeznaczone są do tego dwa porty. Jedne dla klientów uruchamianych jako aplikacje www a drugi dla aplikacji połączonych w "stałym połączeniu" dla aplikacji tzw. SCADA. Komunikacja pomiędzy klientami a SimaticServer odbywa się poprzez komendy. Klient www w jednym wywołaniu wysyła jedną komendę, czeka na odpowiedź i zamyka połączenie. Klient SCADA łączy się na cały czas swojej pracy i w sposób ciągły wymienia się z SimaticServer komendami.

Jako że SimaticServer i SimaticScada są pierwszymi aplikacjami napisanymi przeze mnie w Java/SE/Swing a na początku prac nie wiadomo było w jakim kierunku pójdą - jest parę miejsc wymagających przebudowy. Obsługa strumieni z socket, system komend by komendy samodzielnie nie komunikowały się poprzez strumień, watek do obsługi SCADA nie powinien być odpytywany przez klienta ale powinien wysyłać informacje samodzielnie po stwierdzeniu zmian.
Pakiet zawiera też trochę kodu nie użytego w ostatecznym rozwiązaniu który zostanie przejrzany i usunięty. Prawdopodobnie ulegnie zmianie kolejność zmiennych w buforze komendy.