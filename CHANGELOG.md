# Silent Utils Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.7]

### Added

- `MathUtils.doubleIsInt` and `MathUtils.floatIsInt`
- TextUtils

## [1.0.?]

### Added

- Anchor enum

### Fixed

- EnumValue using "toString" instead of "name" (fixes loading of enums that override toString)

## [1.0.0] - 2019-02-10

First public release

### Added

- Config system. Somewhat similar to the 1.13 Forge config system, but a bit more flexible.
- Color class (originally from Silent Lib)
- MathUtils class (originally from Silent Lib)
