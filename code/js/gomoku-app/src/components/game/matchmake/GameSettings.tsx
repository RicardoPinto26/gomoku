import React, {createContext, useContext, useEffect, useState} from "react";
import {Lobby} from "../../../domain/Lobby";

export interface GameSettings {
    name: string
    gridSize: number;
    winningLength: number;
    opening: string;
    pointsMargin: number;
    overflow: boolean;
}


export function from(lobby: Lobby): GameSettings {
    return {
        name: "unknown",
        gridSize: lobby.gridSize,
        winningLength: lobby.winningLength,
        opening: lobby.opening,
        pointsMargin: lobby.pointsMargin,
        overflow: lobby.overflow
    };
}


export const defaultSettings: GameSettings = {
    name: "Unnamed",
    gridSize: 15,
    winningLength: 5,
    opening: "SWAP2",
    pointsMargin: 200,
    overflow: true,
};

const GameSettingsContext = createContext<{
    settings: GameSettings;
    setSettings: React.Dispatch<React.SetStateAction<GameSettings>>;
}>({
    settings: defaultSettings,
    setSettings: () => {
    },
});

export const useMatchmakingConfig = () => useContext(GameSettingsContext);
export const useCreateLobbyConfig = () => useContext(GameSettingsContext);


export function CreateGameConfigProvider({children}: { children: React.ReactNode }) {
    const [settings, setSettings] = useState<GameSettings>(() => {
        const localData = localStorage.getItem('createGameSettings')
        return localData ? JSON.parse(localData) : defaultSettings
    });

    useEffect(() => {
        localStorage.setItem('createGameSettings', JSON.stringify(settings))
    }, [settings])

    return (
        <GameSettingsContext.Provider value={{settings: settings, setSettings}}>
            {children}
        </GameSettingsContext.Provider>
    );
}


export function MatchmakeConfigProvider({children}: { children: React.ReactNode }) {
    const [settings, setSettings] = useState<GameSettings>(() => {
        const localData = localStorage.getItem('matchmakingSettings')
        return localData ? JSON.parse(localData) : defaultSettings
    });

    useEffect(() => {
        localStorage.setItem('matchmakingSettings', JSON.stringify(settings))
    }, [settings])

    return (
        <GameSettingsContext.Provider value={{settings: settings, setSettings}}>
            {children}
        </GameSettingsContext.Provider>
    );
}
