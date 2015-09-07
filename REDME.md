


Komendy:

Proces wykonuje komendy nadsyłane do SimaticServer w postaci strumienia bajtowego. 
Pierwsze dwa bajty opisują kod komendy.

Mapa kodów:

wewnętrzne biblioteki pjpl.s7

	000000000000000B -> 0000111111111111B => 0x0000 -> 0x0FFF =>      0 ->  4095 

		0 : Odmowa, negacja. Odpowiedź "Nie" na dowolne pytanie oczekujące tylko potwierdzenia.
		1 : Zgoda, potwierdzenie. Odpowiedź "Tak" na dowolne pytanie oczekujące tylko potwierdzenia.

nieprzydzielone:

	000100000000000B -> 0001111111111111B => 0x1000 -> 0x1FFF =>   4096 ->  8191
	001000000000000B -> 0011111111111111B => 0x2000 -> 0x2FFF =>   8192 -> 16383
	010000000000000B -> 0111111111111111B => 0x4000 -> 0x7FFF =>  16384 -> 32767
	
	
na potrzeby aplikacji	
	
	od SimaticXXX do SimaticServer
	100000000000000B -> 1011111111111111B => 0x8000 -> 0xBFFF =>  32768 -> 49151
	
	od SimaticServer -> SimaticXXX
	110000000000000B -> 1111111111111111B => 0xC000 -> 0xCFFF =>  49152 -> 65535
