# Marvel App Android

## The Project

This app was developed using the Marvel Developer API (link: https://developer.marvel.com/). With it you can:

1. Explore a list with all the Marvel characters;
2. Search for characters by name;
3. Save all your favorites characters;

## Requirements
In order to build this project, you'll need a `PUBLIC_KEY` and a `PRIVATE_KEY` to consume from Marvel API. Once you have theses keys, just add them to your project's local properties.

### Get API Keys
If you don't have the Marvel API Keys, you can generate them on the Marvel Developer website (link: https://developer.marvel.com/account). All you need to do is create an account for you (it is free!).

### Add API Keys To Project
With the Marvel API Keys in hand, you want to add them to your project's local properties file. Add the following properties:
* `PUBLIC_KEY`: will contain the string value for your Public API Key
* `PRIVATE_KEY`: will contain the string value for your Private API Key

#### Example
Suppose your generated Public Key is equal to `on1et2woth3ree` and your Private Key is `fou4rf5ivesix6`. Then you should add the following properties configuration to your `local.properties` file:
`PUBLIC_KEY="on1et2woth3ree"`
`PRIVATE_KEY="fou4rf5ivesix6"`
