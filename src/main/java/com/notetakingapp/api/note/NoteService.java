package com.notetakingapp.api.note;

import com.notetakingapp.api.Utils.crypto.AES;
import com.notetakingapp.api.Utils.crypto.RSA;
import com.notetakingapp.api.user.Key;
import com.notetakingapp.api.user.KeyRepository;
import com.notetakingapp.api.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final KeyRepository keyRepository;
    private final UserRepository userRepository;

    public List<Note> getNotes() throws Exception {
        List<Note> all_notes = noteRepository.findAll();
        List<NoteDto> noteDtoList = NoteMapper.getNoteDtos(all_notes);
        return NoteMapper.getNotes(decryptNote(noteDtoList));
    }

    public Note getNote(String userId) throws Exception {
        Optional<Note> note = noteRepository.findByNoteId(userId);
        NoteDto noteDto = NoteMapper.getNoteDto(note.orElseThrow());
        return NoteMapper.getNote(decryptNote(noteDto));
    }

    public List<Note> getNotesById(String userId) throws Exception {
        List<Note> all_notes = noteRepository.findNotesByUserId(userId);
        List<NoteDto> noteDtoList = NoteMapper.getNoteDtos(all_notes);
        return NoteMapper.getNotes(decryptNote(noteDtoList));
    }

    public Note createNote(NoteDto noteDto) {
        noteDto.setNoteId(UUID.randomUUID().toString());
        Note note = NoteMapper.getNote(noteDto);
        return noteRepository.save(note);
    }

    public Note updateNote(NoteDto noteDto) {
        Note note = NoteMapper.getNote(noteDto);
        return noteRepository.save(note);
    }

    public String getSymmetricKey(String userId) throws Exception {
        Optional<Key> key = keyRepository.findKeyByUserId(userId);
        if (key.isPresent()) {
            Key k = key.get();
            String salt1Key = k.getSalt1Key();
            String salt2Key = k.getSalt2Key();
            String c2_prk = k.getPrivateKey();
            String c1_prk = AES.decrypt(c2_prk, salt1Key);
            String p_prk = AES.decrypt(c1_prk, salt2Key);

            return RSA.decrypt(k.getSymmetricKey(), p_prk);
        }
        return "";
    }

    public List<NoteDto> decryptNote(List<NoteDto> noteDtoList) throws Exception {
        for (NoteDto note : noteDtoList) {
            String symmetricKey = getSymmetricKey(note.getUserId());
            if (symmetricKey.isEmpty())
                throw new NullPointerException();
            note.setTitle(AES.decrypt(note.getTitle(), symmetricKey));
            note.setBody(AES.decrypt(note.getBody(), symmetricKey));
        }
        return noteDtoList;
    }

    public NoteDto decryptNote(NoteDto noteDto) throws Exception {
        String symmetricKey = getSymmetricKey(noteDto.getUserId());
        if (symmetricKey.isEmpty())
            throw new NullPointerException();
        noteDto.setTitle(AES.decrypt(noteDto.getTitle(), symmetricKey));
        noteDto.setBody(AES.decrypt(noteDto.getBody(), symmetricKey));
        return noteDto;
    }

    public NoteDto encryptNote(NoteDto noteDto) throws Exception {
        String symmetricKey = getSymmetricKey(noteDto.getUserId());
        if (symmetricKey.isEmpty())
            throw new NullPointerException();
        noteDto.setTitle(AES.encrypt(noteDto.getTitle(), symmetricKey));
        noteDto.setBody(AES.encrypt(noteDto.getBody(), symmetricKey));
        return noteDto;
    }

    @Transactional
    public String deleteNote(String id) {
        noteRepository.deleteById(id);
        return "OK";
    }

}
