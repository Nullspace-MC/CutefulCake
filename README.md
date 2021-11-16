# CutefulCake
An attempt at a backport of fabric-carpet for 1.8.9

## How to use :
You will need to set up a Legacy Fabric/Quilt server, and add the mod to the mods folder
see https://github.com/Legacy-Fabric

If you have any issue, or suggestion, you can either open an issue/pull request on github, or join [my (sarah's) server](https://discord.gg/FCQrwXy) or [Nullspace's server](https://discord.gg/VS4dM2B)

## What it adds :

### Rules :
`explosionNoBlockDamage`
* Disables explosions breaking blocks
* Type : boolean
* Default : false

`explosionRandomRatio`
* Sets the ray size multiplier by this value
* Type : float
* Default : -1.0
* Must be set either to -1.0 or a number within 0 and 1

`loggerRefreshRate`
* Sets at which frequency the loggers' values are recalculated
* Type : int
* Default : 20
* Must be above 0

`hopperCounters`
* Enables hopper counters
* Type : boolean
* Default : false

### Loggers :
`tps`
* Allows you to see tps and mspt of server
* No parameters

`counter`
* Allows you to see the content of a hopper counter
* Parameters are the different wool colors
* Cannot log without a parameter

### Commands added :

| Command name | Description                                             |
| ------------ | ------------------------------------------------------- |
| cake         | Allows you to set values of rules                       |
| log          | Allows you to subscribe to a logger                     |
| player       | Allows you to spawn fake players                        |
| counter      | Allows you to see the info of a counter, or to reset it |

## License

This mod is available under the MIT License
