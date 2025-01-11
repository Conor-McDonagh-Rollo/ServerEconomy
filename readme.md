# ServerEconomy

ServerEconomy is a 1.19 Minecraft spigot plugin designed to manage in-game economy features, providing server administrators with tools to handle virtual currencies and related functionalities.

## Features

- **Economy Management**: Facilitates the creation, modification, and deletion of player balances.
- **Leaderboard**: Leaderboard so that players may compare balances to each other.
- **Grand Exchange**: Features a full implementation of a Runescape-like grand exchange marketplace.

## Installation

1. Download the latest release of ServerEconomy from the [Releases page](https://github.com/Conor-McDonagh-Rollo/ServerEconomy/releases).
2. Place the downloaded `.jar` file into your server's `plugins` directory.
3. Restart your Minecraft server to load the plugin.

## Configuration

After installation, a configuration file will be generated in the `plugins/ServerEconomy` directory. This file saves player balances and grand exchange items.

## Commands

Here is a list of available commands along with their aliases:

- **`/balance`** (`/bal`): Check your current balance.
- **`/pay <player> <amount>`**: Transfer funds to another player.
- **`/addfunds <player> <amount>`**: Add funds to a player's balance (requires appropriate permissions).
- **`/setfunds <player> <amount>`**: Set a player's balance to a specific amount (requires appropriate permissions).
- **`/grandexchange`** (`/ge`): Access the grand exchange market interface.
- **`/sellhand`** (`/sh`): Sell the item currently held in your hand.
- **`/leaderboard`** (`/lb`): View the top players by balance.

*Note: Ensure you have the necessary permissions to execute these commands.*

## Permissions

Here are the permissions associated with the commands:

- **`seconomy.balance`**: Permission to check your balance.
- **`seconomy.pay`**: Permission to transfer funds to other players.
- **`seconomy.addfunds`**: Permission to add funds to a player's balance.
- **`seconomy.setfunds`**: Permission to set a player's balance.
- **`seconomy.grandexchange`**: Permission to access the grand exchange market.
- **`seconomy.sellhand`**: Permission to sell the item in hand.
- **`seconomy.leaderboard`**: Permission to view the balance leaderboard.

*Ensure that players have the appropriate permissions to use these commands.*

## Building from Source

To build ServerEconomy from source, follow these steps:

1. **Clone the repository**:

```bash
git clone https://github.com/Conor-McDonagh-Rollo/ServerEconomy.git
```
2. Navigate to the project directory:
```bash
cd ServerEconomy
```
3. Check the pom.xml for version information:

4. Open the pom.xml file to review the project version and dependencies.

5. Build the project using Maven:

6. Ensure you have Maven installed on your system, then run:
```bash
    mvn clean package

    Locate the built .jar file:

    After a successful build, the plugin .jar file will be located in the target directory.

    Deploy the plugin:

    Copy the .jar file to your server's plugins directory and restart the server.
```
## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.

## License

This project is licensed under the MIT License. See the LICENSE file for details.