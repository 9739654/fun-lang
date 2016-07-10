
# komentarz

# funkcja
def funkcjaA()
	a = 2
	b = 3
	c = a + b
	d string
	println("a+b=" + c);

funkcjaA()		# wywołanie funkcji



# funkcja z parametrem i zwracaniem wartości
def funkcjaB(a int, b int)
	c = a + b
	return c

res: int
res = funkcjaB(3,4)	# przypisanie wartości funkcji
println(res)		# wywołanie funkcji z parametrem



# Lambda
funkcjaD: def()
funkcjaD = def () println("D")		// Lambda jednolinijkowa
funkcjaE = def ()			# Lambda wielolinijkowa
	x = 2
	println("E: ", x)
	return x



# Wywołanie
(def (a: int, b: int) println("sum=" + (a+b)))(4,5)	# wypisze: 9



# Lambda wywołanie
dd = funkcjaD()
ee = funkcjaD()



# klasa
def KlasaA
	poleA int
	poleB string
	poleC int
	
	def wypisz()
		println(poleA + " " + poleB + " " + poleC)

obiektA = KlasaA()	# utwórz instncję klasy
obiektA.wypisz()	# wywołanie metody na obiekcie
obiektA.poleA = 2	# błąd: pole prywatne



# klasa z parametrem
def KlasaB(a string, b string)
	nazwa: string
	
	def KlasaB()
		if a == ""
			nazwa = b
		elif a == "x"
			nazwa = "x"
		else
			nazwa = a
	
	def długość(): int
		return a.length()



obiektA = KlasaA()	# utwórz instancję KlasaA
obiektA.int = 2

funkcjaA = FunkcjaA()	# Wywołaj FunkcjaA i przypisz wynik do funkcjaA
# funkcjaA == 5

FunkcjaC = FunkcjaA
funcC = funkcjaC()
# funcC == 5

xyz = FunkcjaB(3,4);
# xyz == 7

instKlB = KlasaB("Nazwa")
length = instKlB.długość

"napis"
"wieloliniowy
napis"
"wieloliniowy napis
z escape \"sscharacterem"
"i napis z dwoma
escape \\ characterami"
