# Java Trip Assistance (JTA)
___
## Introduction
Þetta verkefni byggir á eldri verkefni úr áfanganum Viðmótsforritun 2026, þar sem markmiðið var að hanna einfalt ferðaplanunarforrit.
Í því verkefni þurfti að birta grunnupplýsingar eins og:
- Titill
- Áfangastaður
- Dagsetning

Þessari útgáfu verður verkefnið þróað til að handla betur við stærri ferðalögum, og leyfa notendum að setja upp allskonar upplýsingum sem kemur alltaf fram þegar það er unnið við að skipulegga ferðum.

---

## About

- Q: Hvað á þessari kerfi í raunini að gera?
> A: Þessi á að aðstoða manneskja með því að geta verið svoleiðis tól til að skipulaga ferðalögum. Manneskjan getur slá inn hóp upplýsingum, hvenær það byrjar o.fl.

- Q: Hvernig virkar það?
> A: Manneskja skrár sér inn með þeirri eigin reiking, eða býr til nýja reiking til að komast inn í main view. Ef manneskjan ætlar að skipulega nýtt ferð þá verður ýtt á hnappan "New Trip", sem krefur notendur að slá nokkrar upplýsingum. Manneskjan getur líka skrá inn valkost upplýsingum með því að ýta á "Optionals".

- Q: Ég vill eyða reikinguna mína út, er það hægt?
> A: Já, það er hægt að gera því. Manneskjan skrár sér inn í reikingin og síðan þegar hann er komin í main-view þarf hann að ýta á hnappan sem er með "gír" mynd á sér, þarna velur hann milli að skrá sér út eða eyða reikingu út, og síðan staðfestir hann hvort að hann vill að gera þess.

---

## Executing the program

---

### Skref 1
> Manneskjan þarf að komast á directory "__**verkefni**__", sem á að vera t.d.: **"C:\Users\Notandi\Documents\GitHub\vidmotsforritun-hi-2026-v4-lab\Verkefni"** til að koma með dæmi.
> 
> Þegar slóðinn stendur því, þá á manneskjan að þurfa keyra efitrfarandi  prompts inn í þeirra terminal (hvort það sé IntelliJ eða líkt):
> 
> `mvn clean`
>
> `mvn compile`
> 
> `mvn javafx:run`
> 
> Manneskjan eiga geta líka bara gert þessu allt í einnri prompt
> 
> `mvn clean compile javafx:run`
> 
> Þegar það er búið getur tekið forritið frá 5-15 sekundur að ræsa sér, þá þolinmæði er kröfun með þessu.

### Skref 2
> Það munn tveimur gluggar opnast þegar kerfið er ræst 100%, fyrsti er main-view og síðan er login gluggin. Main-view verður tómur og munn ekki birta engan gögn til að manneskjan skrár sér inn.
> Manneskjan getur valið milli að gera það eða skrá sér með nýja reiking.

### Skref 3
> Þegar manneskjan er búin með þess og býr til nýja reiking / log inn, þá á að birtast main-view og nafni þeirra efst.

---
