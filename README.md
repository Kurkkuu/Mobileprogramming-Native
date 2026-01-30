# Mobiiliohjelmointi natiiviteknologialla

# Week2 ViewModel

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

### Näin se toimii tässä sovelluksessa:

```kotlin
// Repository: MutableStateFlow sisäiseen käyttöön
private val _tasks = MutableStateFlow&lt;List&lt;Task&gt;&gt;(MockData.tasks)

// Julkinen StateFlow UI:lle
val tasks: StateFlow&lt;List&lt;Task&gt;&gt; = _tasks.asStateFlow()

// Päivitys lähettää uuden arvon kaikille kuuntelijoille
_tasks.value = _tasks.value + newTask
