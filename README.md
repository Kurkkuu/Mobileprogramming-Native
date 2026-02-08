# Mobiiliohjelmointi natiiviteknologialla

# Week2 ViewModel
[Week2DemoVideo](https://youtu.be/_2vSW-P1zs4)

## Compose-tilanhallinta

Compose-tilanhallinta on tärkeä osa Jetpack Compose:a. Se perustuu reaktiiviseen arkkitehtuuriin, joka tarkoittaa, että UI-painotukset päivittyvät automaattisesti, kun tila muuttuu.

### Miten Compose-tilanhallinta toimii?

Compose-tilanhallinta perustuu seuraaviin periaatteisiin:

1. **Reaktiiviset tiedot (State)**
2. **Rekompositio (Recomposition)**
3. **Immutaability (Immutaatio)**

### ViewModel vs remember

`remember` on Compose-funktio, joka säilyttää tilan `@Composable`-funktioiden välillä. Se on hyödyllinen paikallisen tilan hallintaan, mutta se ei ole paras ratkaisu koko sovelluksen tilan hallintaan.

#### Miksi ViewModel on parempi kuin pelkkä remember?
1. **Elinkaaren hallinta**
2. **Tilan jakaminen**
3. **Testaus**
4. **Järkevä arkkitehtuuri**
   
# Week 3 - MVVM ja StateFlow
[Week3DemoVideo](https://youtu.be/9toVEbTUlFM)

## MVVM-arkkitehtuuri

**MVVM** (Model-View-ViewModel) jakaa sovelluksen kolmeen kerrokseen:

- **Model**: Data ja business-logiikka (`Task` data class, `TaskRepository`)
- **View**: UI-komponentit (`HomeScreen`, `DetailScreen` Composable-funktiot)  
- **ViewModel**: Välittäjä kerrosten välillä (`TaskViewModel`), hallinnoi UI-tilaa

### Miksi MVVM on hyödyllinen Compose-sovelluksissa?

1. **Erota vastuut**: Composablet keskittyvät vain näyttämiseen, logiikka on erikseen ViewModelissa
2. **Tilanhallinta**: ViewModel säilyttää datan kiertäessä näyttöä (esim. vaakatilaan)
3. **Reaktiivisuus**: UI päivittyy automaattisesti kun ViewModelin data muuttuu

## StateFlow toiminta

**StateFlow** on tilallinen virta, joka säilyttää aina viimeisimmän arvon.

### Esimerkki:

```kotlin
// Repository: MutableStateFlow sisäiseen käyttöön
private val _tasks = MutableStateFlow&lt;List&lt;Task&gt;&gt;(MockData.tasks)

// Julkinen StateFlow UI:lle
val tasks: StateFlow&lt;List&lt;Task&gt;&gt; = _tasks.asStateFlow()

// Päivitys lähettää uuden arvon kaikille kuuntelijoille
_tasks.value = _tasks.value + newTask
```
# Week 4 - Navigointi

[Week4DemoVideo](https://youtube.com/shorts/4FOa8JR__gU)

## Navigointi jetpack composessa

Navigointi mahdollistaa siirtymisen eri näkymien välillä.

#### Keskeiset komponentit
1. NavController: Hallinnoi navigaatiotilaa
2. NavHost: Määrittelee reitit
3. Routes: ("home", "calendar", "settings")
   
#### Navigaatiorakenne
1. HomeScreen: Tehtävälista
2. CalendarScreen: Tehtävät kalenterinäkymässä
3. SettingsScreen: Sovelluksen tiedot
   
## MVVM + navigointi
Sama ViewModel jaetaan kaikkien näkymien kesken. ViewModel luodaan MainActivity:ssä, joten se säilyy koko navigaation ajan.

```kotlin
// ViewModel luodaan kerran MainActivityssä
val viewModel: TaskViewModel = viewModel()

// Välitetään kaikille screeneille
composable(Routes.HOME) { HomeScreen(viewModel = viewModel, ...) }
composable(Routes.CALENDAR) { CalendarScreen(viewModel = viewModel, ...) }
```
## CalendarScreen
```kotlin
tasks.groupBy { it.dueDate }  // Map<String, List<Task>>
```
Päivämäärät on otsikoita ja niiden alla on kyseisen päivän tehtävät

## Dialogit vs navigointi
Dialogit on overlay-komponentteja, jotka näkyvät nykyisen näkymän päällä ilman navigaatiota.
