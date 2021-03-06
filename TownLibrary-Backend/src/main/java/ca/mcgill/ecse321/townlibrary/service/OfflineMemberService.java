package ca.mcgill.ecse321.townlibrary.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.*;

import java.util.*;

@Service
public class OfflineMemberService {

    @Autowired
    private OfflineMemberRepository offlineMemberRepository;

    /**
     * Creates a offline member
     *
     * @param lib       The library (non-null)
     * @param name      The member's name (non-empty)
     * @param address   The member's address (non-empty)
     *
     * @return          The member instance
     *
     * @throws IllegalArgumentException problems with the arguments (comma separated)
     */
    @Transactional
    public OfflineMember createOfflineMember(Library lib, String name, String address) {
        final StringBuilder errmsg = new StringBuilder();
        if (lib == null)
            errmsg.append("NULL-LIBRARY,");
        if (name != null)
            name = name.trim();
        if (name == null || name.isEmpty())
            errmsg.append("EMPTY-NAME,");
        if (address != null)
            address = address.trim();
        if (address == null || address.isEmpty())
            errmsg.append("EMPTY-ADDRESS,");

        if (errmsg.length() != 0) {
            // Delete the trailing ","
            errmsg.deleteCharAt(errmsg.length() - 1);
            throw new IllegalArgumentException(errmsg.toString());
        }

        final OfflineMember u = new OfflineMember();
        u.setName(name);
        u.setAddress(address);
        u.setLibrary(lib);
        this.offlineMemberRepository.save(u);
        return u;
    }

    /**
     * Sets the in town field of a offline member
     *
     * @param id    The id
     * @param flag  The new field value
     *
     * @return      The offline member after patching
     */
    @Transactional
    public OfflineMember setOfflineMemberInTownStatus(int id, boolean flag) {
        final OfflineMember u = this.offlineMemberRepository.findById(id).orElse(null);
        if (u == null)
            throw new IllegalArgumentException("NOT-FOUND-OFFLINE-MEMBER");

        u.setInTown(flag);
        this.offlineMemberRepository.save(u);
        return u;
    }

    /**
     * Retrieves a offline member by its id.
     *
     * @param id    The id
     *
     * @return      The offline member or null if no such id exists
     */
    @Transactional
    public OfflineMember getOfflineMember(int id) {
        return this.offlineMemberRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all the offline members registered under this system.
     *
     * @return all the offline members
     */
    @Transactional
    public List<OfflineMember> getAllOfflineMembers() {
        final ArrayList<OfflineMember> list = new ArrayList<>();
        for (final OfflineMember u : this.offlineMemberRepository.findAll())
            list.add(u);
        return list;
    }
}