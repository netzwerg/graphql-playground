module Main exposing (..)

import Html exposing (Html, text, div)
import Http
import Json.Decode as Decode exposing (..)
import Json.Encode as Encode exposing (..)

main =
    Html.program
        { init = init
        , view = view
        , update = update
        , subscriptions = \_ -> Sub.none
        }


type alias Model =
    { response : String
    }

type alias Location =
    { id: Int
    , x: Int
    , y: Int
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
    [ ("query", Encode.string query) ]


init : ( Model, Cmd Msg )
init =
    { response = "Waiting for a response... " } ! [ Http.send FetchLocations request ]


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        FetchLocations (Ok res) ->
            { response = toString res } ! []

        FetchLocations (Err res) ->
            { response = toString res } ! []


view : Model -> Html Msg
view model =
    div [] [ text model.response ]
