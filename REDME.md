


Komendy:

Proces wykonuje komendy nadsyłane do SimaticServer w postaci strumienia bajtowego. 
Pierwsze dwa bajty opisują kod komendy.

Mata kodów:

000000000000000B -> 0000111111111111B => 0x0000 -> 0x0FFF =>      0 ->  4095 wewnętrzne biblioteki pjpl.s7

	0 : Odmowa, negacja. Odpowiedź "Nie" na dowolne pytanie oczekujące jedynie potwierdzenia.
	1 : Zgoda, potwierdzenie. Odpowiedź "Tak" na dowolne pytanie oczekujące tylko potwierdzenia.
	
000100000000000B -> 0001111111111111B => 0x1000 -> 0x1FFF =>   4096 ->  8191
001000000000000B -> 0011111111111111B => 0x2000 -> 0x2FFF =>   8192 -> 16383
010000000000000B -> 0111111111111111B => 0x4000 -> 0x7FFF =>  16384 -> 32767
100000000000000B -> 1111111111111111B => 0x8000 -> 0x8FFF =>  32768 -> 65535 na potrzeby aplikacji
