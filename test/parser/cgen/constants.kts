import ce.defs.*
import generators.obj.input.*

when (target()) {
    ce.defs.Target.Kotlin -> setOutputBasePath("../kotlin/")
    ce.defs.Target.Cxx -> setOutputBasePath("../cxx/")
    else -> {}
}

namespace("com.goldman.dt1").apply {
    constantsBlock("GrumpyCat").apply {
        addBlockComment("GrumpyCat constants definition block")
        defaultType(DataType.int8)
        add("Grumpy", -23)
    }

    constantsBlock("SillyCat").apply {
        addBlockComment("SillyCat constants definition block")
        defaultType(DataType.int16)
        add("Grumpy", -32)
        add("Silly", -32)
    }

    constantsBlock("WildCat").apply {
        addBlockComment("WildCat constants definition block")
        defaultType(DataType.int32)
        add("Grumpy", -63)
        add("Silly", -63)
        add("Wild", -63)
    }

    constantsBlock("RedCat").apply {
        addBlockComment("RedCat constants definition block")
        defaultType(DataType.int64)
        add("Grumpy", -128)
        add("Silly", -128)
        add("Wild", -128)
        add("Red", -128)
    }

    constantsBlock("BrownCat").apply {
        addBlockComment("BrownCat constants definition block")
        defaultType(DataType.uint8)
        add("Grumpy", 23)
        add("Silly", 23)
        add("Wild", 23)
        add("Red", 23)
        add("Brown", 23)
    }

    constantsBlock("TastyCat").apply {
        addBlockComment("TastyCat constants definition block")
        defaultType(DataType.uint16)
        add("Grumpy", 32)
        add("Silly", 32)
        add("Wild", 32)
        add("Red", 32)
        add("Brown", 32)
        add("Tasty", 32)
    }

    constantsBlock("WiseCat").apply {
        addBlockComment("WiseCat constants definition block")
        defaultType(DataType.uint32)
        add("Grumpy", 128)
        add("Silly", 128)
        add("Wild", 128)
        add("Red", 128)
        add("Brown", 128)
        add("Tasty", 128)
        add("Wise", 128)
    }

    constantsBlock("WindyCat").apply {
        addBlockComment("WindyCat constants definition block")
        defaultType(DataType.uint64)
        add("Grumpy", 243)
        add("Silly", 243)
        add("Wild", 243)
        add("Red", 243)
        add("Brown", 243)
        add("Tasty", 243)
        add("Wise", 243)
        add("Windy", 243)
    }

    constantsBlock("CloudyCat").apply {
        addBlockComment("CloudyCat constants definition block")
        defaultType(DataType.float32)
        add("Grumpy", 23.45)
        add("Silly", 23.45)
        add("Wild", 23.45)
        add("Red", 23.45)
        add("Brown", 23.45)
        add("Tasty", 23.45)
        add("Wise", 23.45)
        add("Windy", 23.45)
        add("Cloudy", 23.45)
    }

    constantsBlock("NobleCat").apply {
        addBlockComment("NobleCat constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.91)
        add("Silly", 23.91)
        add("Wild", 23.91)
        add("Red", 23.91)
        add("Brown", 23.91)
        add("Tasty", 23.91)
        add("Wise", 23.91)
        add("Windy", 23.91)
        add("Cloudy", 23.91)
        add("Noble", 23.91)
    }

    constantsBlock("AngryCat").apply {
        addBlockComment("AngryCat constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.751)
        add("Silly", 23.751)
        add("Wild", 23.751)
        add("Red", 23.751)
        add("Brown", 23.751)
        add("Tasty", 23.751)
        add("Wise", 23.751)
        add("Windy", 23.751)
        add("Cloudy", 23.751)
        add("Noble", 23.751)
        add("Angry", 23.751)
    }

    constantsBlock("CrazyCat").apply {
        addBlockComment("CrazyCat constants definition block")
        defaultType(DataType.int8)
        add("Grumpy", -23)
        add("Silly", -23)
        add("Wild", -23)
        add("Red", -23)
        add("Brown", -23)
        add("Tasty", -23)
        add("Wise", -23)
        add("Windy", -23)
        add("Cloudy", -23)
        add("Noble", -23)
        add("Angry", -23)
        add("Crazy", -23)
    }

    constantsBlock("GrumpyDog").apply {
        addBlockComment("GrumpyDog constants definition block")
        defaultType(DataType.int16)
        add("Grumpy", -32)
        add("Silly", -32)
    }

    constantsBlock("SillyDog").apply {
        addBlockComment("SillyDog constants definition block")
        defaultType(DataType.int32)
        add("Grumpy", -63)
        add("Silly", -63)
        add("Wild", -63)
    }

    constantsBlock("WildDog").apply {
        addBlockComment("WildDog constants definition block")
        defaultType(DataType.int64)
        add("Grumpy", -128)
        add("Silly", -128)
        add("Wild", -128)
        add("Red", -128)
    }

    constantsBlock("RedDog").apply {
        addBlockComment("RedDog constants definition block")
        defaultType(DataType.uint8)
        add("Grumpy", 23)
        add("Silly", 23)
        add("Wild", 23)
        add("Red", 23)
        add("Brown", 23)
    }

    constantsBlock("BrownDog").apply {
        addBlockComment("BrownDog constants definition block")
        defaultType(DataType.uint16)
        add("Grumpy", 32)
        add("Silly", 32)
        add("Wild", 32)
        add("Red", 32)
        add("Brown", 32)
        add("Tasty", 32)
    }

    constantsBlock("TastyDog").apply {
        addBlockComment("TastyDog constants definition block")
        defaultType(DataType.uint32)
        add("Grumpy", 128)
        add("Silly", 128)
        add("Wild", 128)
        add("Red", 128)
        add("Brown", 128)
        add("Tasty", 128)
        add("Wise", 128)
    }

    constantsBlock("WiseDog").apply {
        addBlockComment("WiseDog constants definition block")
        defaultType(DataType.uint64)
        add("Grumpy", 243)
        add("Silly", 243)
        add("Wild", 243)
        add("Red", 243)
        add("Brown", 243)
        add("Tasty", 243)
        add("Wise", 243)
        add("Windy", 243)
    }

    constantsBlock("WindyDog").apply {
        addBlockComment("WindyDog constants definition block")
        defaultType(DataType.float32)
        add("Grumpy", 23.45)
        add("Silly", 23.45)
        add("Wild", 23.45)
        add("Red", 23.45)
        add("Brown", 23.45)
        add("Tasty", 23.45)
        add("Wise", 23.45)
        add("Windy", 23.45)
        add("Cloudy", 23.45)
    }

    constantsBlock("CloudyDog").apply {
        addBlockComment("CloudyDog constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.91)
        add("Silly", 23.91)
        add("Wild", 23.91)
        add("Red", 23.91)
        add("Brown", 23.91)
        add("Tasty", 23.91)
        add("Wise", 23.91)
        add("Windy", 23.91)
        add("Cloudy", 23.91)
        add("Noble", 23.91)
    }

    constantsBlock("NobleDog").apply {
        addBlockComment("NobleDog constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.751)
        add("Silly", 23.751)
        add("Wild", 23.751)
        add("Red", 23.751)
        add("Brown", 23.751)
        add("Tasty", 23.751)
        add("Wise", 23.751)
        add("Windy", 23.751)
        add("Cloudy", 23.751)
        add("Noble", 23.751)
        add("Angry", 23.751)
    }

    constantsBlock("AngryDog").apply {
        addBlockComment("AngryDog constants definition block")
        defaultType(DataType.int8)
        add("Grumpy", -23)
        add("Silly", -23)
        add("Wild", -23)
        add("Red", -23)
        add("Brown", -23)
        add("Tasty", -23)
        add("Wise", -23)
        add("Windy", -23)
        add("Cloudy", -23)
        add("Noble", -23)
        add("Angry", -23)
        add("Crazy", -23)
    }

    constantsBlock("CrazyDog").apply {
        addBlockComment("CrazyDog constants definition block")
        defaultType(DataType.int16)
        add("Grumpy", -32)
    }

    constantsBlock("GrumpyFish").apply {
        addBlockComment("GrumpyFish constants definition block")
        defaultType(DataType.int32)
        add("Grumpy", -63)
        add("Silly", -63)
        add("Wild", -63)
    }

    constantsBlock("SillyFish").apply {
        addBlockComment("SillyFish constants definition block")
        defaultType(DataType.int64)
        add("Grumpy", -128)
        add("Silly", -128)
        add("Wild", -128)
        add("Red", -128)
    }

    constantsBlock("WildFish").apply {
        addBlockComment("WildFish constants definition block")
        defaultType(DataType.uint8)
        add("Grumpy", 23)
        add("Silly", 23)
        add("Wild", 23)
        add("Red", 23)
        add("Brown", 23)
    }

    constantsBlock("RedFish").apply {
        addBlockComment("RedFish constants definition block")
        defaultType(DataType.uint16)
        add("Grumpy", 32)
        add("Silly", 32)
        add("Wild", 32)
        add("Red", 32)
        add("Brown", 32)
        add("Tasty", 32)
    }

    constantsBlock("BrownFish").apply {
        addBlockComment("BrownFish constants definition block")
        defaultType(DataType.uint32)
        add("Grumpy", 128)
        add("Silly", 128)
        add("Wild", 128)
        add("Red", 128)
        add("Brown", 128)
        add("Tasty", 128)
        add("Wise", 128)
    }

    constantsBlock("TastyFish").apply {
        addBlockComment("TastyFish constants definition block")
        defaultType(DataType.uint64)
        add("Grumpy", 243)
        add("Silly", 243)
        add("Wild", 243)
        add("Red", 243)
        add("Brown", 243)
        add("Tasty", 243)
        add("Wise", 243)
        add("Windy", 243)
    }

    constantsBlock("WiseFish").apply {
        addBlockComment("WiseFish constants definition block")
        defaultType(DataType.float32)
        add("Grumpy", 23.45)
        add("Silly", 23.45)
        add("Wild", 23.45)
        add("Red", 23.45)
        add("Brown", 23.45)
        add("Tasty", 23.45)
        add("Wise", 23.45)
        add("Windy", 23.45)
        add("Cloudy", 23.45)
    }

    constantsBlock("WindyFish").apply {
        addBlockComment("WindyFish constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.91)
        add("Silly", 23.91)
        add("Wild", 23.91)
        add("Red", 23.91)
        add("Brown", 23.91)
        add("Tasty", 23.91)
        add("Wise", 23.91)
        add("Windy", 23.91)
        add("Cloudy", 23.91)
        add("Noble", 23.91)
    }

    constantsBlock("CloudyFish").apply {
        addBlockComment("CloudyFish constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.751)
        add("Silly", 23.751)
        add("Wild", 23.751)
        add("Red", 23.751)
        add("Brown", 23.751)
        add("Tasty", 23.751)
        add("Wise", 23.751)
        add("Windy", 23.751)
        add("Cloudy", 23.751)
        add("Noble", 23.751)
        add("Angry", 23.751)
    }

    constantsBlock("NobleFish").apply {
        addBlockComment("NobleFish constants definition block")
        defaultType(DataType.int8)
        add("Grumpy", -23)
        add("Silly", -23)
        add("Wild", -23)
        add("Red", -23)
        add("Brown", -23)
        add("Tasty", -23)
        add("Wise", -23)
        add("Windy", -23)
        add("Cloudy", -23)
        add("Noble", -23)
        add("Angry", -23)
        add("Crazy", -23)
    }

    constantsBlock("AngryFish").apply {
        addBlockComment("AngryFish constants definition block")
        defaultType(DataType.int16)
        add("Grumpy", -32)
    }

    constantsBlock("CrazyFish").apply {
        addBlockComment("CrazyFish constants definition block")
        defaultType(DataType.int32)
        add("Grumpy", -63)
        add("Silly", -63)
    }

    constantsBlock("GrumpyCake").apply {
        addBlockComment("GrumpyCake constants definition block")
        defaultType(DataType.int64)
        add("Grumpy", -128)
        add("Silly", -128)
        add("Wild", -128)
        add("Red", -128)
    }

    constantsBlock("SillyCake").apply {
        addBlockComment("SillyCake constants definition block")
        defaultType(DataType.uint8)
        add("Grumpy", 23)
        add("Silly", 23)
        add("Wild", 23)
        add("Red", 23)
        add("Brown", 23)
    }

    constantsBlock("WildCake").apply {
        addBlockComment("WildCake constants definition block")
        defaultType(DataType.uint16)
        add("Grumpy", 32)
        add("Silly", 32)
        add("Wild", 32)
        add("Red", 32)
        add("Brown", 32)
        add("Tasty", 32)
    }

    constantsBlock("RedCake").apply {
        addBlockComment("RedCake constants definition block")
        defaultType(DataType.uint32)
        add("Grumpy", 128)
        add("Silly", 128)
        add("Wild", 128)
        add("Red", 128)
        add("Brown", 128)
        add("Tasty", 128)
        add("Wise", 128)
    }

    constantsBlock("BrownCake").apply {
        addBlockComment("BrownCake constants definition block")
        defaultType(DataType.uint64)
        add("Grumpy", 243)
        add("Silly", 243)
        add("Wild", 243)
        add("Red", 243)
        add("Brown", 243)
        add("Tasty", 243)
        add("Wise", 243)
        add("Windy", 243)
    }

    constantsBlock("TastyCake").apply {
        addBlockComment("TastyCake constants definition block")
        defaultType(DataType.float32)
        add("Grumpy", 23.45)
        add("Silly", 23.45)
        add("Wild", 23.45)
        add("Red", 23.45)
        add("Brown", 23.45)
        add("Tasty", 23.45)
        add("Wise", 23.45)
        add("Windy", 23.45)
        add("Cloudy", 23.45)
    }

    constantsBlock("WiseCake").apply {
        addBlockComment("WiseCake constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.91)
        add("Silly", 23.91)
        add("Wild", 23.91)
        add("Red", 23.91)
        add("Brown", 23.91)
        add("Tasty", 23.91)
        add("Wise", 23.91)
        add("Windy", 23.91)
        add("Cloudy", 23.91)
        add("Noble", 23.91)
    }

    constantsBlock("WindyCake").apply {
        addBlockComment("WindyCake constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.751)
        add("Silly", 23.751)
        add("Wild", 23.751)
        add("Red", 23.751)
        add("Brown", 23.751)
        add("Tasty", 23.751)
        add("Wise", 23.751)
        add("Windy", 23.751)
        add("Cloudy", 23.751)
        add("Noble", 23.751)
        add("Angry", 23.751)
    }

    constantsBlock("CloudyCake").apply {
        addBlockComment("CloudyCake constants definition block")
        defaultType(DataType.int8)
        add("Grumpy", -23)
        add("Silly", -23)
        add("Wild", -23)
        add("Red", -23)
        add("Brown", -23)
        add("Tasty", -23)
        add("Wise", -23)
        add("Windy", -23)
        add("Cloudy", -23)
        add("Noble", -23)
        add("Angry", -23)
        add("Crazy", -23)
    }

    constantsBlock("NobleCake").apply {
        addBlockComment("NobleCake constants definition block")
        defaultType(DataType.int16)
        add("Grumpy", -32)
    }

    constantsBlock("AngryCake").apply {
        addBlockComment("AngryCake constants definition block")
        defaultType(DataType.int32)
        add("Grumpy", -63)
        add("Silly", -63)
    }

    constantsBlock("CrazyCake").apply {
        addBlockComment("CrazyCake constants definition block")
        defaultType(DataType.int64)
        add("Grumpy", -128)
        add("Silly", -128)
        add("Wild", -128)
    }

    constantsBlock("GrumpyDoor").apply {
        addBlockComment("GrumpyDoor constants definition block")
        defaultType(DataType.uint8)
        add("Grumpy", 23)
        add("Silly", 23)
        add("Wild", 23)
        add("Red", 23)
        add("Brown", 23)
    }

    constantsBlock("SillyDoor").apply {
        addBlockComment("SillyDoor constants definition block")
        defaultType(DataType.uint16)
        add("Grumpy", 32)
        add("Silly", 32)
        add("Wild", 32)
        add("Red", 32)
        add("Brown", 32)
        add("Tasty", 32)
    }

    constantsBlock("WildDoor").apply {
        addBlockComment("WildDoor constants definition block")
        defaultType(DataType.uint32)
        add("Grumpy", 128)
        add("Silly", 128)
        add("Wild", 128)
        add("Red", 128)
        add("Brown", 128)
        add("Tasty", 128)
        add("Wise", 128)
    }

    constantsBlock("RedDoor").apply {
        addBlockComment("RedDoor constants definition block")
        defaultType(DataType.uint64)
        add("Grumpy", 243)
        add("Silly", 243)
        add("Wild", 243)
        add("Red", 243)
        add("Brown", 243)
        add("Tasty", 243)
        add("Wise", 243)
        add("Windy", 243)
    }

    constantsBlock("BrownDoor").apply {
        addBlockComment("BrownDoor constants definition block")
        defaultType(DataType.float32)
        add("Grumpy", 23.45)
        add("Silly", 23.45)
        add("Wild", 23.45)
        add("Red", 23.45)
        add("Brown", 23.45)
        add("Tasty", 23.45)
        add("Wise", 23.45)
        add("Windy", 23.45)
        add("Cloudy", 23.45)
    }

    constantsBlock("TastyDoor").apply {
        addBlockComment("TastyDoor constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.91)
        add("Silly", 23.91)
        add("Wild", 23.91)
        add("Red", 23.91)
        add("Brown", 23.91)
        add("Tasty", 23.91)
        add("Wise", 23.91)
        add("Windy", 23.91)
        add("Cloudy", 23.91)
        add("Noble", 23.91)
    }

    constantsBlock("WiseDoor").apply {
        addBlockComment("WiseDoor constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.751)
        add("Silly", 23.751)
        add("Wild", 23.751)
        add("Red", 23.751)
        add("Brown", 23.751)
        add("Tasty", 23.751)
        add("Wise", 23.751)
        add("Windy", 23.751)
        add("Cloudy", 23.751)
        add("Noble", 23.751)
        add("Angry", 23.751)
    }

    constantsBlock("WindyDoor").apply {
        addBlockComment("WindyDoor constants definition block")
        defaultType(DataType.int8)
        add("Grumpy", -23)
        add("Silly", -23)
        add("Wild", -23)
        add("Red", -23)
        add("Brown", -23)
        add("Tasty", -23)
        add("Wise", -23)
        add("Windy", -23)
        add("Cloudy", -23)
        add("Noble", -23)
        add("Angry", -23)
        add("Crazy", -23)
    }

    constantsBlock("CloudyDoor").apply {
        addBlockComment("CloudyDoor constants definition block")
        defaultType(DataType.int16)
        add("Grumpy", -32)
    }

    constantsBlock("NobleDoor").apply {
        addBlockComment("NobleDoor constants definition block")
        defaultType(DataType.int32)
        add("Grumpy", -63)
        add("Silly", -63)
    }

    constantsBlock("AngryDoor").apply {
        addBlockComment("AngryDoor constants definition block")
        defaultType(DataType.int64)
        add("Grumpy", -128)
        add("Silly", -128)
        add("Wild", -128)
    }

    constantsBlock("CrazyDoor").apply {
        addBlockComment("CrazyDoor constants definition block")
        defaultType(DataType.uint8)
        add("Grumpy", 23)
        add("Silly", 23)
        add("Wild", 23)
        add("Red", 23)
    }

    constantsBlock("GrumpyCar").apply {
        addBlockComment("GrumpyCar constants definition block")
        defaultType(DataType.uint16)
        add("Grumpy", 32)
        add("Silly", 32)
        add("Wild", 32)
        add("Red", 32)
        add("Brown", 32)
        add("Tasty", 32)
    }

    constantsBlock("SillyCar").apply {
        addBlockComment("SillyCar constants definition block")
        defaultType(DataType.uint32)
        add("Grumpy", 128)
        add("Silly", 128)
        add("Wild", 128)
        add("Red", 128)
        add("Brown", 128)
        add("Tasty", 128)
        add("Wise", 128)
    }

    constantsBlock("WildCar").apply {
        addBlockComment("WildCar constants definition block")
        defaultType(DataType.uint64)
        add("Grumpy", 243)
        add("Silly", 243)
        add("Wild", 243)
        add("Red", 243)
        add("Brown", 243)
        add("Tasty", 243)
        add("Wise", 243)
        add("Windy", 243)
    }

    constantsBlock("RedCar").apply {
        addBlockComment("RedCar constants definition block")
        defaultType(DataType.float32)
        add("Grumpy", 23.45)
        add("Silly", 23.45)
        add("Wild", 23.45)
        add("Red", 23.45)
        add("Brown", 23.45)
        add("Tasty", 23.45)
        add("Wise", 23.45)
        add("Windy", 23.45)
        add("Cloudy", 23.45)
    }

    constantsBlock("BrownCar").apply {
        addBlockComment("BrownCar constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.91)
        add("Silly", 23.91)
        add("Wild", 23.91)
        add("Red", 23.91)
        add("Brown", 23.91)
        add("Tasty", 23.91)
        add("Wise", 23.91)
        add("Windy", 23.91)
        add("Cloudy", 23.91)
        add("Noble", 23.91)
    }

    constantsBlock("TastyCar").apply {
        addBlockComment("TastyCar constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.751)
        add("Silly", 23.751)
        add("Wild", 23.751)
        add("Red", 23.751)
        add("Brown", 23.751)
        add("Tasty", 23.751)
        add("Wise", 23.751)
        add("Windy", 23.751)
        add("Cloudy", 23.751)
        add("Noble", 23.751)
        add("Angry", 23.751)
    }

    constantsBlock("WiseCar").apply {
        addBlockComment("WiseCar constants definition block")
        defaultType(DataType.int8)
        add("Grumpy", -23)
        add("Silly", -23)
        add("Wild", -23)
        add("Red", -23)
        add("Brown", -23)
        add("Tasty", -23)
        add("Wise", -23)
        add("Windy", -23)
        add("Cloudy", -23)
        add("Noble", -23)
        add("Angry", -23)
        add("Crazy", -23)
    }

    constantsBlock("WindyCar").apply {
        addBlockComment("WindyCar constants definition block")
        defaultType(DataType.int16)
        add("Grumpy", -32)
    }

    constantsBlock("CloudyCar").apply {
        addBlockComment("CloudyCar constants definition block")
        defaultType(DataType.int32)
        add("Grumpy", -63)
        add("Silly", -63)
    }

    constantsBlock("NobleCar").apply {
        addBlockComment("NobleCar constants definition block")
        defaultType(DataType.int64)
        add("Grumpy", -128)
        add("Silly", -128)
        add("Wild", -128)
    }

    constantsBlock("AngryCar").apply {
        addBlockComment("AngryCar constants definition block")
        defaultType(DataType.uint8)
        add("Grumpy", 23)
        add("Silly", 23)
        add("Wild", 23)
        add("Red", 23)
    }

    constantsBlock("CrazyCar").apply {
        addBlockComment("CrazyCar constants definition block")
        defaultType(DataType.uint16)
        add("Grumpy", 32)
        add("Silly", 32)
        add("Wild", 32)
        add("Red", 32)
        add("Brown", 32)
    }

    constantsBlock("GrumpyLion").apply {
        addBlockComment("GrumpyLion constants definition block")
        defaultType(DataType.uint32)
        add("Grumpy", 128)
        add("Silly", 128)
        add("Wild", 128)
        add("Red", 128)
        add("Brown", 128)
        add("Tasty", 128)
        add("Wise", 128)
    }

    constantsBlock("SillyLion").apply {
        addBlockComment("SillyLion constants definition block")
        defaultType(DataType.uint64)
        add("Grumpy", 243)
        add("Silly", 243)
        add("Wild", 243)
        add("Red", 243)
        add("Brown", 243)
        add("Tasty", 243)
        add("Wise", 243)
        add("Windy", 243)
    }

    constantsBlock("WildLion").apply {
        addBlockComment("WildLion constants definition block")
        defaultType(DataType.float32)
        add("Grumpy", 23.45)
        add("Silly", 23.45)
        add("Wild", 23.45)
        add("Red", 23.45)
        add("Brown", 23.45)
        add("Tasty", 23.45)
        add("Wise", 23.45)
        add("Windy", 23.45)
        add("Cloudy", 23.45)
    }

    constantsBlock("RedLion").apply {
        addBlockComment("RedLion constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.91)
        add("Silly", 23.91)
        add("Wild", 23.91)
        add("Red", 23.91)
        add("Brown", 23.91)
        add("Tasty", 23.91)
        add("Wise", 23.91)
        add("Windy", 23.91)
        add("Cloudy", 23.91)
        add("Noble", 23.91)
    }

    constantsBlock("BrownLion").apply {
        addBlockComment("BrownLion constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.751)
        add("Silly", 23.751)
        add("Wild", 23.751)
        add("Red", 23.751)
        add("Brown", 23.751)
        add("Tasty", 23.751)
        add("Wise", 23.751)
        add("Windy", 23.751)
        add("Cloudy", 23.751)
        add("Noble", 23.751)
        add("Angry", 23.751)
    }

    constantsBlock("TastyLion").apply {
        addBlockComment("TastyLion constants definition block")
        defaultType(DataType.int8)
        add("Grumpy", -23)
        add("Silly", -23)
        add("Wild", -23)
        add("Red", -23)
        add("Brown", -23)
        add("Tasty", -23)
        add("Wise", -23)
        add("Windy", -23)
        add("Cloudy", -23)
        add("Noble", -23)
        add("Angry", -23)
        add("Crazy", -23)
    }

    constantsBlock("WiseLion").apply {
        addBlockComment("WiseLion constants definition block")
        defaultType(DataType.int16)
        add("Grumpy", -32)
    }

    constantsBlock("WindyLion").apply {
        addBlockComment("WindyLion constants definition block")
        defaultType(DataType.int32)
        add("Grumpy", -63)
        add("Silly", -63)
    }

    constantsBlock("CloudyLion").apply {
        addBlockComment("CloudyLion constants definition block")
        defaultType(DataType.int64)
        add("Grumpy", -128)
        add("Silly", -128)
        add("Wild", -128)
    }

    constantsBlock("NobleLion").apply {
        addBlockComment("NobleLion constants definition block")
        defaultType(DataType.uint8)
        add("Grumpy", 23)
        add("Silly", 23)
        add("Wild", 23)
        add("Red", 23)
    }

    constantsBlock("AngryLion").apply {
        addBlockComment("AngryLion constants definition block")
        defaultType(DataType.uint16)
        add("Grumpy", 32)
        add("Silly", 32)
        add("Wild", 32)
        add("Red", 32)
        add("Brown", 32)
    }

    constantsBlock("CrazyLion").apply {
        addBlockComment("CrazyLion constants definition block")
        defaultType(DataType.uint32)
        add("Grumpy", 128)
        add("Silly", 128)
        add("Wild", 128)
        add("Red", 128)
        add("Brown", 128)
        add("Tasty", 128)
    }

    constantsBlock("GrumpyPanther").apply {
        addBlockComment("GrumpyPanther constants definition block")
        defaultType(DataType.uint64)
        add("Grumpy", 243)
        add("Silly", 243)
        add("Wild", 243)
        add("Red", 243)
        add("Brown", 243)
        add("Tasty", 243)
        add("Wise", 243)
        add("Windy", 243)
    }

    constantsBlock("SillyPanther").apply {
        addBlockComment("SillyPanther constants definition block")
        defaultType(DataType.float32)
        add("Grumpy", 23.45)
        add("Silly", 23.45)
        add("Wild", 23.45)
        add("Red", 23.45)
        add("Brown", 23.45)
        add("Tasty", 23.45)
        add("Wise", 23.45)
        add("Windy", 23.45)
        add("Cloudy", 23.45)
    }

    constantsBlock("WildPanther").apply {
        addBlockComment("WildPanther constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.91)
        add("Silly", 23.91)
        add("Wild", 23.91)
        add("Red", 23.91)
        add("Brown", 23.91)
        add("Tasty", 23.91)
        add("Wise", 23.91)
        add("Windy", 23.91)
        add("Cloudy", 23.91)
        add("Noble", 23.91)
    }

    constantsBlock("RedPanther").apply {
        addBlockComment("RedPanther constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.751)
        add("Silly", 23.751)
        add("Wild", 23.751)
        add("Red", 23.751)
        add("Brown", 23.751)
        add("Tasty", 23.751)
        add("Wise", 23.751)
        add("Windy", 23.751)
        add("Cloudy", 23.751)
        add("Noble", 23.751)
        add("Angry", 23.751)
    }

    constantsBlock("BrownPanther").apply {
        addBlockComment("BrownPanther constants definition block")
        defaultType(DataType.int8)
        add("Grumpy", -23)
        add("Silly", -23)
        add("Wild", -23)
        add("Red", -23)
        add("Brown", -23)
        add("Tasty", -23)
        add("Wise", -23)
        add("Windy", -23)
        add("Cloudy", -23)
        add("Noble", -23)
        add("Angry", -23)
        add("Crazy", -23)
    }

    constantsBlock("TastyPanther").apply {
        addBlockComment("TastyPanther constants definition block")
        defaultType(DataType.int16)
        add("Grumpy", -32)
    }

    constantsBlock("WisePanther").apply {
        addBlockComment("WisePanther constants definition block")
        defaultType(DataType.int32)
        add("Grumpy", -63)
        add("Silly", -63)
    }

    constantsBlock("WindyPanther").apply {
        addBlockComment("WindyPanther constants definition block")
        defaultType(DataType.int64)
        add("Grumpy", -128)
        add("Silly", -128)
        add("Wild", -128)
    }

    constantsBlock("CloudyPanther").apply {
        addBlockComment("CloudyPanther constants definition block")
        defaultType(DataType.uint8)
        add("Grumpy", 23)
        add("Silly", 23)
        add("Wild", 23)
        add("Red", 23)
    }

    constantsBlock("NoblePanther").apply {
        addBlockComment("NoblePanther constants definition block")
        defaultType(DataType.uint16)
        add("Grumpy", 32)
        add("Silly", 32)
        add("Wild", 32)
        add("Red", 32)
        add("Brown", 32)
    }

    constantsBlock("AngryPanther").apply {
        addBlockComment("AngryPanther constants definition block")
        defaultType(DataType.uint32)
        add("Grumpy", 128)
        add("Silly", 128)
        add("Wild", 128)
        add("Red", 128)
        add("Brown", 128)
        add("Tasty", 128)
    }

    constantsBlock("CrazyPanther").apply {
        addBlockComment("CrazyPanther constants definition block")
        defaultType(DataType.uint64)
        add("Grumpy", 243)
        add("Silly", 243)
        add("Wild", 243)
        add("Red", 243)
        add("Brown", 243)
        add("Tasty", 243)
        add("Wise", 243)
    }

    constantsBlock("GrumpyPlanet").apply {
        addBlockComment("GrumpyPlanet constants definition block")
        defaultType(DataType.float32)
        add("Grumpy", 23.45)
        add("Silly", 23.45)
        add("Wild", 23.45)
        add("Red", 23.45)
        add("Brown", 23.45)
        add("Tasty", 23.45)
        add("Wise", 23.45)
        add("Windy", 23.45)
        add("Cloudy", 23.45)
    }

    constantsBlock("SillyPlanet").apply {
        addBlockComment("SillyPlanet constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.91)
        add("Silly", 23.91)
        add("Wild", 23.91)
        add("Red", 23.91)
        add("Brown", 23.91)
        add("Tasty", 23.91)
        add("Wise", 23.91)
        add("Windy", 23.91)
        add("Cloudy", 23.91)
        add("Noble", 23.91)
    }

    constantsBlock("WildPlanet").apply {
        addBlockComment("WildPlanet constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.751)
        add("Silly", 23.751)
        add("Wild", 23.751)
        add("Red", 23.751)
        add("Brown", 23.751)
        add("Tasty", 23.751)
        add("Wise", 23.751)
        add("Windy", 23.751)
        add("Cloudy", 23.751)
        add("Noble", 23.751)
        add("Angry", 23.751)
    }

    constantsBlock("RedPlanet").apply {
        addBlockComment("RedPlanet constants definition block")
        defaultType(DataType.int8)
        add("Grumpy", -23)
        add("Silly", -23)
        add("Wild", -23)
        add("Red", -23)
        add("Brown", -23)
        add("Tasty", -23)
        add("Wise", -23)
        add("Windy", -23)
        add("Cloudy", -23)
        add("Noble", -23)
        add("Angry", -23)
        add("Crazy", -23)
    }

    constantsBlock("BrownPlanet").apply {
        addBlockComment("BrownPlanet constants definition block")
        defaultType(DataType.int16)
        add("Grumpy", -32)
    }

    constantsBlock("TastyPlanet").apply {
        addBlockComment("TastyPlanet constants definition block")
        defaultType(DataType.int32)
        add("Grumpy", -63)
        add("Silly", -63)
    }

    constantsBlock("WisePlanet").apply {
        addBlockComment("WisePlanet constants definition block")
        defaultType(DataType.int64)
        add("Grumpy", -128)
        add("Silly", -128)
        add("Wild", -128)
    }

    constantsBlock("WindyPlanet").apply {
        addBlockComment("WindyPlanet constants definition block")
        defaultType(DataType.uint8)
        add("Grumpy", 23)
        add("Silly", 23)
        add("Wild", 23)
        add("Red", 23)
    }

    constantsBlock("CloudyPlanet").apply {
        addBlockComment("CloudyPlanet constants definition block")
        defaultType(DataType.uint16)
        add("Grumpy", 32)
        add("Silly", 32)
        add("Wild", 32)
        add("Red", 32)
        add("Brown", 32)
    }

    constantsBlock("NoblePlanet").apply {
        addBlockComment("NoblePlanet constants definition block")
        defaultType(DataType.uint32)
        add("Grumpy", 128)
        add("Silly", 128)
        add("Wild", 128)
        add("Red", 128)
        add("Brown", 128)
        add("Tasty", 128)
    }

    constantsBlock("AngryPlanet").apply {
        addBlockComment("AngryPlanet constants definition block")
        defaultType(DataType.uint64)
        add("Grumpy", 243)
        add("Silly", 243)
        add("Wild", 243)
        add("Red", 243)
        add("Brown", 243)
        add("Tasty", 243)
        add("Wise", 243)
    }

    constantsBlock("CrazyPlanet").apply {
        addBlockComment("CrazyPlanet constants definition block")
        defaultType(DataType.float32)
        add("Grumpy", 23.45)
        add("Silly", 23.45)
        add("Wild", 23.45)
        add("Red", 23.45)
        add("Brown", 23.45)
        add("Tasty", 23.45)
        add("Wise", 23.45)
        add("Windy", 23.45)
    }

    constantsBlock("GrumpySun").apply {
        addBlockComment("GrumpySun constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.91)
        add("Silly", 23.91)
        add("Wild", 23.91)
        add("Red", 23.91)
        add("Brown", 23.91)
        add("Tasty", 23.91)
        add("Wise", 23.91)
        add("Windy", 23.91)
        add("Cloudy", 23.91)
        add("Noble", 23.91)
    }

    constantsBlock("SillySun").apply {
        addBlockComment("SillySun constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.751)
        add("Silly", 23.751)
        add("Wild", 23.751)
        add("Red", 23.751)
        add("Brown", 23.751)
        add("Tasty", 23.751)
        add("Wise", 23.751)
        add("Windy", 23.751)
        add("Cloudy", 23.751)
        add("Noble", 23.751)
        add("Angry", 23.751)
    }

    constantsBlock("WildSun").apply {
        addBlockComment("WildSun constants definition block")
        defaultType(DataType.int8)
        add("Grumpy", -23)
        add("Silly", -23)
        add("Wild", -23)
        add("Red", -23)
        add("Brown", -23)
        add("Tasty", -23)
        add("Wise", -23)
        add("Windy", -23)
        add("Cloudy", -23)
        add("Noble", -23)
        add("Angry", -23)
        add("Crazy", -23)
    }

    constantsBlock("RedSun").apply {
        addBlockComment("RedSun constants definition block")
        defaultType(DataType.int16)
        add("Grumpy", -32)
    }

    constantsBlock("BrownSun").apply {
        addBlockComment("BrownSun constants definition block")
        defaultType(DataType.int32)
        add("Grumpy", -63)
        add("Silly", -63)
    }

    constantsBlock("TastySun").apply {
        addBlockComment("TastySun constants definition block")
        defaultType(DataType.int64)
        add("Grumpy", -128)
        add("Silly", -128)
        add("Wild", -128)
    }

    constantsBlock("WiseSun").apply {
        addBlockComment("WiseSun constants definition block")
        defaultType(DataType.uint8)
        add("Grumpy", 23)
        add("Silly", 23)
        add("Wild", 23)
        add("Red", 23)
    }

    constantsBlock("WindySun").apply {
        addBlockComment("WindySun constants definition block")
        defaultType(DataType.uint16)
        add("Grumpy", 32)
        add("Silly", 32)
        add("Wild", 32)
        add("Red", 32)
        add("Brown", 32)
    }

    constantsBlock("CloudySun").apply {
        addBlockComment("CloudySun constants definition block")
        defaultType(DataType.uint32)
        add("Grumpy", 128)
        add("Silly", 128)
        add("Wild", 128)
        add("Red", 128)
        add("Brown", 128)
        add("Tasty", 128)
    }

    constantsBlock("NobleSun").apply {
        addBlockComment("NobleSun constants definition block")
        defaultType(DataType.uint64)
        add("Grumpy", 243)
        add("Silly", 243)
        add("Wild", 243)
        add("Red", 243)
        add("Brown", 243)
        add("Tasty", 243)
        add("Wise", 243)
    }

    constantsBlock("AngrySun").apply {
        addBlockComment("AngrySun constants definition block")
        defaultType(DataType.float32)
        add("Grumpy", 23.45)
        add("Silly", 23.45)
        add("Wild", 23.45)
        add("Red", 23.45)
        add("Brown", 23.45)
        add("Tasty", 23.45)
        add("Wise", 23.45)
        add("Windy", 23.45)
    }

    constantsBlock("CrazySun").apply {
        addBlockComment("CrazySun constants definition block")
        defaultType(DataType.float64)
        add("Grumpy", 23.91)
        add("Silly", 23.91)
        add("Wild", 23.91)
        add("Red", 23.91)
        add("Brown", 23.91)
        add("Tasty", 23.91)
        add("Wise", 23.91)
        add("Windy", 23.91)
        add("Cloudy", 23.91)
    }

}