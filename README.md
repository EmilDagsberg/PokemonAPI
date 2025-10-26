![Pokemon Banner](https://assets.pokemon.com/assets/cms2/img/misc/countries/pt/country_detail_pokemon.png)

# PokemonAPI

PokemonAPI is a Java-based backend application, that takes information from [Poke API](https://pokeapi.co/) to get data into our database.
It fetches:
  - Pokemon Name
  - Pokemon Type
  - Pokemon Location

# What can it do?

  - The program works like this. We have a populator that populates the database with the 151 pokemons from the 1st gen of the series.
  - After this we have made the normal CRUD endpoints.
  - Added some more unique ones including:
    - getRandomPokemonByType()
    - getRandomPokemon()
    - addPokemonToTeam()
    - and more...

# Technology used

- **Java 17**
- **Hibernatae/JPA**
- **Jakarta Persistence**
- **Lombok**
- **Junit**
- **DOCKER**
- **RestAssured**


# TESTING

- There are tests in the given Java test folder, both HTTP endpoint tests and JUnit tests.
- You can also try the endpoints yourself [Here](https://pkmon.fourthingsilove.dk/api/pokemon/)
