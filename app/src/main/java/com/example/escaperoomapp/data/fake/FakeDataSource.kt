package com.example.escaperoomapp.data.fake

import com.example.escaperoomapp.data.model.Puzzle
import com.example.escaperoomapp.data.model.Room

object FakeDataSource {

    val rooms = listOf(
        Room(
            id = "1",
            name = "Záhadná laboratoř",
            description = "Proberte se z bezvědomí v tajné laboratoři.",
            difficulty = "Střední",
            timeLimit = 30,
            imageUrl = "https://picsum.photos/seed/lab/800/400",
            youtubeUrl = "https://www.youtube.com/watch?v=qzGxK6Uiu04"
        ),
        Room(
            id = "2",
            name = "Strašidelný hrad",
            description = "Uvízli jste v opuštěném hradě plném záhad.",
            difficulty = "Těžká",
            timeLimit = 45,
            imageUrl = "https://picsum.photos/seed/castle/800/400",
            youtubeUrl = "https://www.youtube.com/watch?v=qzGxK6Uiu04"
        ),
        Room(
            id = "3",
            name = "Vesmírná stanice",
            description = "Systémy stanice selhávají. Zachraňte posádku.",
            difficulty = "Lehká",
            timeLimit = 20,
            imageUrl = "https://picsum.photos/seed/space/800/400",
            youtubeUrl = "https://www.youtube.com/watch?v=qzGxK6Uiu04"
        )
    )

    val puzzles = listOf(
        Puzzle(
            id = "1", roomId = "1",
            question = "Která barva chybí v duze?",
            type = "choice",
            optionsRaw = "Červená,Fialová,Zelená,Oranžová",
            correctAnswer = "Fialová",
            hint = "Vzpomeňte si na ROYGBIV.",
            codeWord = null
        ),
        Puzzle(
            id = "2", roomId = "1",
            question = "ROK PRVNÍ MISE NA MĚSÍC.",
            type = "code",
            optionsRaw = null,
            correctAnswer = "1969",
            hint = "Apollo 11 přistálo na Měsíci v létě tohoto roku.",
            codeWord = "APOLLO"
        ),
        Puzzle(
            id = "3", roomId = "1",
            question = "Která planeta je největší?",
            type = "choice",
            optionsRaw = "Saturn,Jupiter,Neptun,Uran",
            correctAnswer = "Jupiter",
            hint = "Má velkou červenou skvrnu.",
            codeWord = null
        ),
        Puzzle(
            id = "4", roomId = "2",
            question = "POČET DNÍ V PŘESTUPNÉM ROCE.",
            type = "code",
            optionsRaw = null,
            correctAnswer = "366",
            hint = "Přestupný rok má o jeden den více.",
            codeWord = "LEAP"
        ),
        Puzzle(
            id = "5", roomId = "2",
            question = "Která zbraň sloužila k obléhání hradů?",
            type = "choice",
            optionsRaw = "Katapult,Kuše,Halapartna,Rapír",
            correctAnswer = "Katapult",
            hint = "Vrhala kameny na hradby.",
            codeWord = null
        ),
        Puzzle(
            id = "6", roomId = "2",
            question = "LATINSKÝ NÁZEV VODY.",
            type = "code",
            optionsRaw = null,
            correctAnswer = "AQUA",
            hint = "Znáte z pojmu aquapark.",
            codeWord = "PERGAMEN"
        ),
        Puzzle(
            id = "7", roomId = "3",
            question = "Kolik planet má sluneční soustava?",
            type = "choice",
            optionsRaw = "7,8,9,10",
            correctAnswer = "8",
            hint = "Pluto bylo přeřazeno v 2006.",
            codeWord = null
        ),
        Puzzle(
            id = "8", roomId = "3",
            question = "RYCHLOST SVĚTLA (km/s).",
            type = "code",
            optionsRaw = null,
            correctAnswer = "300000",
            hint = "Světlo urazí 300 000 km za sekundu.",
            codeWord = "REAKTOR"
        ),
        Puzzle(
            id = "9", roomId = "3",
            question = "Který plyn tvoří největší část atmosféry?",
            type = "choice",
            optionsRaw = "Kyslík,Dusík,Argon,CO2",
            correctAnswer = "Dusík",
            hint = "Tvoří přibližně 78 % atmosféry.",
            codeWord = null
        )
    )
}