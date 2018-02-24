module Main exposing (..)

import Html exposing (Html, div, text)
import Http
import Json.Decode as Decode exposing (..)
import Json.Encode as Encode exposing (..)
import Svg exposing (..)
import Svg.Attributes exposing (..)
import Time exposing (Time, second)


main =
    Html.program
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        }


type alias Model =
    { time : Time
    , locations : List Location
    }


type alias Location =
    { id : Int
    , x : Int
    , y : Int
    }


locationDecoder : Decode.Decoder Location
locationDecoder =
    Decode.map3 Location
        (Decode.field "id" Decode.int)
        (Decode.field "x" Decode.int)
        (Decode.field "y" Decode.int)


allLocations : String
allLocations =
    """
    {
      allLocations {
        id,
        x,
        y
      }
    }
    """


type Msg
    = FetchLocations (Result Http.Error (List Location))
    | Tick Time


request : Http.Request (List Location)
request =
    let
        url =
            "http://localhost:8080/graphql"

        body =
            Http.jsonBody (jsonQuery allLocations)
    in
    Http.post url body (Decode.field "data" (Decode.field "allLocations" (Decode.list locationDecoder)))


jsonQuery : String -> Encode.Value
jsonQuery query =
    Encode.object
        [ ( "query", Encode.string query ) ]


init : ( Model, Cmd Msg )
init =
    { time = 0
    , locations = []
    }
        ! [ Http.send FetchLocations request ]


view : Model -> Html Msg
view model =
    svg [ viewBox "-0.2 -0.2 1.4 1.4", height "300px" ]
        [ viewLocations model.locations :: [(viewLatestLocation (List.head (List.reverse model.locations)))] |> g []]


viewLocations : List Location -> Svg Msg
viewLocations locations =
    locations
        |> List.map (\l -> circle [ cx <| toString l.x, cy <| toString l.y, r "0.1", fill "#0B79CE" ] [])
        |> g []


viewLatestLocation : Maybe Location -> Svg Msg
viewLatestLocation maybeLocation =
    case maybeLocation of
        Just l ->
            circle [ cx <| toString l.x, cy <| toString l.y, r "0.1", fill "black" ] []

        Nothing ->
            g [] []


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Tick newTime ->
            { model | time = newTime } ! [ Http.send FetchLocations request ]

        FetchLocations (Ok newLocations) ->
            { model | locations = newLocations } ! []

        FetchLocations (Err newLocations) ->
            -- TODO: error handling
            { model | locations = [] } ! []


subscriptions : Model -> Sub Msg
subscriptions model =
    Time.every second Tick
