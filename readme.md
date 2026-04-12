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

## Notendurprófarnir

---

### Verk 1:
> Þegar þú opnar forritið og sér "login", farðu að búa til nýja reiking með því að velja
> "Sign Up" hnappan. Nýja reikingurinn á að innihalda
> - Nafnið þitt 
> - Lykillorð af þinnri val
> 
> Eftir að staðfesta nýja reiking, þú átt að vera færður till main-view

### Verk 2:
> Búðu til tvö ný ferðalög af þinnri eigin valkosti, þú ræðir hvort þú villt setja inn einhvern optional upplýsing inn eða ekki.

## Verk 3
> Skoðaðu annarhvor ferð og athuga ef öll upplýsingar séu rétt. Ef þeir eru, þá skaltu ekki gera neitt, ef það er mistök þá ýttu á "edit trip" til að laga þess

## Verk 4
> Breytu byrjunar dagsetningu til einhvern dags. Síðan breytu endurnardagsetninguna.

## Verk 5
> Eyddu einn af þessari ferðalögum.

## Verk 6
> Skráðu út úr reikinguna þína.

## Verk 7
> Skráðu inn aftur reikinguna þína, síðan áttur að eyða þína reiking.

## Verk 8
> Búðu aftur til reikinguna þína.
