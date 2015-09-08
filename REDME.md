


Komendy:

Proces wykonuje komendy nadsyłane do SimaticServer w postaci strumienia bajtowego. 
Pierwsze dwa bajty opisują kod komendy.

Mapa kodów:

Stałe ogólne:

	000000000000000B -> 0000111111111111B => 0x0000 -> 0x0FFF =>      0 ->  4095 

		0x0000 -> 0000 : Odmowa, negacja. Odpowiedź "Nie" na dowolne pytanie oczekujące tylko potwierdzenia.
		0x0001 -> 0001 : Zgoda, potwierdzenie. Odpowiedź "Tak" na dowolne pytanie oczekujące tylko potwierdzenia.

		
Stałe wewnętrzne biblioteki pjpl.s7

	000100000000000B -> 0001111111111111B => 0x1000 -> 0x1FFF =>   4096 ->  8191	
		
	000100000000000B -> 0x1000 -> 4096
	000100000000001B -> 0x1001 -> 4097
	000100000000010B -> 0x1002 -> 4098
	000100000000011B -> 0x1003 -> 4099

	
nieprzydzielone:

	001000000000000B -> 0011111111111111B => 0x2000 -> 0x3FFF =>   8192 -> 16383
		
sterowanie aplikacją :

	010000000000000B -> 0111111111111111B => 0x4000 -> 0x7FFF =>  16384 -> 32767
	
	
na potrzeby sterowania procesem
	
	od SimaticXXX do SimaticServer
	100000000000000B -> 1011111111111111B => 0x8000 -> 0xBFFF =>  32768 -> 49151
	
	od SimaticServer -> SimaticXXX
	110000000000000B -> 1111111111111111B => 0xC000 -> 0xCFFF =>  49152 -> 65535
